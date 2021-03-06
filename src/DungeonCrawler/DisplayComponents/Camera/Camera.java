package DungeonCrawler.DisplayComponents.Camera;

import DungeonCrawler.GameData;
import DungeonCrawler.GameObject.Entity.Entity;
import DungeonCrawler.GameObject.Wall.Wall;
import DungeonCrawler.Utilities.FastMath;
import DungeonCrawler.Utilities.Image;
import DungeonCrawler.Utilities.Wrappers.MutableDouble;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Draw the level and entities.
public class Camera {
    //A struct that contains the necessary data about the game.
    private final GameData gameData;
    //An buffer that get written to, then displayed on the screen.
    private final BufferedImage canvas;

    //The horizontal and vertical field of view of the camera.
    private final static double FOV = 60;
    //The cameraPosition of the camera.
    private final Point2D.Double cameraPosition;
    //The height of the camera in the game world.
    private final double zPos;
    //The directionAngle of the camera.
    private final MutableDouble cameraAngle;
    //The distance from the camera to the projection plane.
    private final double distProjectionPlane;

    //The raw, live pixel data of the canvas.
    private int[] canvasPixelData;
    //An array containing the slices of the walls that need to be drawn.
    private final ArrayList<ArrayList<ObjectSlice>> wallBuffer;
    //An array full of booleans indicating whether a pixel has been written to or not.
    private Boolean[][] pixelTable;
    //The color of the floor and ceiling.
    private final int floorColor, ceilColor;

    public Camera(GameData gameData, BufferedImage canvas, Point2D.Double position, double zPos, MutableDouble angle) {
        this.canvas = canvas;
        this.gameData = gameData;

        //Get the pixel data from the canvas.
        canvasPixelData = Image.getRGBColorData(canvas);
        //Calculate the distance from the camera to the projection plane from the FOV and image buffer width.
        distProjectionPlane = FastMath.cos(FOV /2) / FastMath.sin(FOV /2) * canvas.getWidth() / 2;
        //Initialize the array of object slices and array of booleans.
        wallBuffer = new ArrayList<>();
        pixelTable = new Boolean[canvas.getWidth()][canvas.getHeight()];

        //Fill the wall buffer with slice objects.
        for(int i = 0; i < canvas.getWidth(); i++)
            wallBuffer.add(new ArrayList<>());

        //Store the camera position and angle.
        this.cameraPosition = position;
        this.zPos = zPos;
        this.cameraAngle = angle;
        //Get the floor and ceiling color.
        this.floorColor = gameData.gameLevel.getFloorColor();
        this.ceilColor = gameData.gameLevel.getCeilColor();

    }

    //Reset the pixel table.
    private void clearPixelTable() {
        for(int i = 0; i < canvas.getWidth(); i++)
            for(int j = 0; j < canvas.getHeight(); j++)
                pixelTable[i][j] = true;
    }

    //Calculate the distance to the point on a wall that the player is looking at.
    private double getDistToWall(Line2D.Double line, double currentRay) {
        //Rotate the wall so that the ray is facing along the y axis.
        FastMath.rotate(line, cameraPosition, -cameraAngle.getValue() - currentRay);
        FastMath.translate(line, -cameraPosition.x, -cameraPosition.y);

        //Return the distance between the camera and the point on the wall that the ray hit. Multiply by cos to remove the fish-eye effect.
        return FastMath.getYIntercept(line) * FastMath.cos(currentRay);
    }

    //Draw a slice of an image.
    private void drawSlice(ObjectSlice slice, int canvasX) {
        //The y coordinate of the image where it will start being drawn.
        double imageStart = 0;
        //The height of the image that actually gets drawn.
        int sliceHeight = (int) (slice.object.getHeight() / slice.distToCamera * distProjectionPlane);

        //The y cameraPosition where the image will be drawn.
        int yPos = (int) Math.round(canvas.getHeight() / 2.0 + (zPos / slice.distToCamera * distProjectionPlane) - (slice.zPos / slice.distToCamera * distProjectionPlane));

        //Calculate the y positions where the wall starts and stops on the screen.
        int wallStart = (int) (yPos - sliceHeight / 2.0);
        int wallEnd = (int) (yPos + sliceHeight / 2.0);

        if (wallStart < 0)
            wallStart = 0;

        if (wallEnd > canvas.getHeight())
            wallEnd = canvas.getHeight();

        //If the image wasn't loaded properly, just draw a purple slice.
        if (slice.image == null) {
            for (int canvasY = wallStart; canvasY < wallEnd; canvasY++)
                if (pixelTable[canvasX][canvasY].equals(true)) {
                    Image.setRGBPixel(canvasPixelData, canvas.getWidth(), canvasX, canvasY, Color.MAGENTA.getRGB());
                    pixelTable[canvasX][canvasY] = false;
                }
        }
        //Otherwise, draw the image.
        else {
            //If the slice is larger than the screen, set the image height and reset the onscreen height.
            if (sliceHeight > canvas.getHeight())
                imageStart = (sliceHeight - canvas.getHeight()) / 2.0 / sliceHeight * slice.image.getHeight();

            //The column of the image that will be drawn.
            int imageX = (int) (slice.intersectRatio * slice.image.getWidth());
            //The row of the image that is going to be drawn.
            int imageY = 0;

            //Loop through the image and draw all of the rows.
            for (int canvasY = wallStart; canvasY < wallEnd; canvasY++) {
                //If the pixel hasn't been written to yet, draw the pixel.
                if (pixelTable[canvasX][canvasY].equals(true)) {
                    //Get the color of the pixel at the current point in the image if the color is valid.
                    int pixelColor = Image.getABGRPixel(slice.imagePixelData, slice.image.getWidth(), imageX, (int)Math.floor(imageStart + imageY / (double)sliceHeight * slice.image.getHeight()));

                    //If the pixel is not transparent, draw the pixel.
                    if ((pixelColor >> 24) != 0x00) {
                        //If the color is not null, tint the pixel.
                        if (slice.imageColor != null)
                            pixelColor = Image.tintABGRPixel(pixelColor, slice.imageColor);

                        //Draw the pixel.
                        Image.setRGBPixel(canvasPixelData, canvas.getWidth(), canvasX, canvasY, pixelColor);
                        pixelTable[canvasX][canvasY] = false;
                    }
                }

                //Move on to the next row to draw.
                imageY++;
            }
        }
    }

    //Fill the given buffer with the slices of wall that needs to be drawn.
    private void calculateWallBuffer() {
        //The directionAngle between each ray.
        double rayAngle = FOV /wallBuffer.size();
        //The directionAngle of the first ray.
        double currentRay = -FOV /2;

        //Variables for calculating the intersection ratio of the wall texture.
        double scaledImgWidth;
        double inGameImgWidth;

        //Iterate through each ray.
        for(int i = 0; i < wallBuffer.size(); i++) {
            //The ArrayList currently being added to.
            ArrayList<ObjectSlice> visibleWalls = wallBuffer.get(i);
            //Clear the ArrayLIst.
            visibleWalls.clear();

            //The furthest wall that the camera will render.
            double furthestWall = 0;

            //Iterate through each wall and determine which one to draw.
            for(Wall wall : gameData.wallList) {
                //Scan the wall if it is visible.
                if(wall.getVisible()) {
                    //Copy the points of the wall.
                    Line2D.Double line = wall.getLine();

                    //Get the distance from the camera to the wall.
                    double dist = getDistToWall(line, currentRay);

                    //If this wall is visible and is closer to the camera than the furthest wall, save it in the buffer.
                    if (dist > 0 && (dist < furthestWall || furthestWall == 0)) {
                        //If the texture for the wall wasn't loaded correctly, put a dummy variable in for inGameImgWidth.
                        if(wall.getTexture() == null)
                            inGameImgWidth = -1;
                        else {
                            //The size of the image fit to the wall.
                            scaledImgWidth = wall.getHeight() / wall.getTexture().getHeight() * wall.getTexture().getWidth();
                            //The width of the image as viewed from the camera.
                            inGameImgWidth = Math.abs(line.x2 - line.x1) / (wall.getWidth()/scaledImgWidth);
                        }

                        //The object slice that will be added to the ArrayList.
                        ObjectSlice currentSlice = new ObjectSlice(wall, dist, wall.getHeight()/2, wall.getTexture(), wall.getTexturePixelData(), wall.getTextureColor(), (Math.abs(line.x1) % inGameImgWidth)/inGameImgWidth);

                        //If there are no walls in the ArrayList yet, add it.
                        if(visibleWalls.isEmpty()) {
                            visibleWalls.add(currentSlice);

                            //If the wall is not see through, set its distance as the farthest distance.
                            if(!wall.isSeeThrough())
                                furthestWall = dist;
                        }
                        //If the wall is see-through, insert in the ArrayList in order.
                        else if(wall.isSeeThrough()) {
                            //The location in the ArrayList where the new slice will be inserted.
                            int j = 0;

                            //Iterate through the ArrayList to where the new slice should be.
                            while(j < visibleWalls.size() && currentSlice.distToCamera >= visibleWalls.get(j).distToCamera)
                                j++;

                            //Insert the new slice.
                            visibleWalls.add(j, currentSlice);
                        }
                        //If the wall is not see-through, remove all walls behind it and then add the new slice to the ArrayList.
                        else {
                            //The location in the ArrayList where the new slice will be inserted.
                            int j = visibleWalls.size() - 1;

                            //Keep removing walls that are further away than the new wall to be added.
                            while(j != -1 && currentSlice.distToCamera <= visibleWalls.get(j).distToCamera) {
                                visibleWalls.remove(j);
                                j--;
                            }

                            //If there are no other objects in the array, just this wall.
                            if(j == -1)
                                visibleWalls.add(currentSlice);
                            //Otherwise, insert the wall in the proper place.
                            else
                                visibleWalls.add(j + 1, currentSlice);

                            //Set its distance as the farthest distance.
                            furthestWall = dist;
                        }
                    }
                }
            }

            //Move on to the next ray.
            currentRay += rayAngle;
        }
    }

    //Draw the wall slices to the wall buffer.
    private void drawWalls() {
        //Iterate through the wall slices and draw them.
        for(int i = 0; i < canvas.getWidth(); i++)
            if(!wallBuffer.get(i).isEmpty())
                drawSlice(wallBuffer.get(i).get(wallBuffer.get(i).size() -  1), i);
    }

    private void drawBackground() {
        for(int i = 0; i < canvas.getWidth(); i++) {
            //Draw the ceiling.
            for (int j = 0; j < canvas.getHeight() / 2; j++)
                if (pixelTable[i][j]) {
                    Image.setRGBPixel(canvasPixelData, canvas.getWidth(), i, j, ceilColor);
                    pixelTable[i][j] = false;
                }

            //Draw the floor.
            for (int j = canvas.getHeight() / 2; j < canvas.getHeight(); j++)
                if (pixelTable[i][j]) {
                    Image.setRGBPixel(canvasPixelData, canvas.getWidth(), i, j, floorColor);
                    pixelTable[i][j] = false;
                }
        }
    }

    //Draw the entities, comparing their distance to the wall buffer.
    private void drawEntitiesAndSeeThrough() {
        //The directionAngle between each ray.
        double rayAngle = FOV /wallBuffer.size();
        //The directionAngle of the first ray.
        double currentRay = -FOV /2;
        //A line representing the entity's location and the width of its image.
        Line2D.Double rotatedLine = new Line2D.Double();
        //The rotated point of the entity to calculate the distance to the center of the it.
        Point2D.Double rotatedPoint = new Point2D.Double();
        //A list of entities that the ray intersects.
        ArrayList<ObjectSlice> visibleEntities = new ArrayList<>();

        //Iterate through each ray.
        for(int i = 0; i < wallBuffer.size(); i++) {
            //Empty the list of entities.
            visibleEntities.clear();

            //Iterate through the entity list and the visible entities to the list.
            for(Entity entity : gameData.entityList) {
                //Scan the entity if it is visible.
                if (entity.getVisible()) {
                    //The directionAngle between the camera and the entity.
                    double entityAngle = Math.toDegrees(Math.atan2(entity.position.x - cameraPosition.x, entity.position.y - cameraPosition.y));

                    //Create a line at the entity's position with the same width. It is horizontal.
                    rotatedLine.setLine(entity.position.x - entity.getWidth() / 2, entity.position.y, entity.position.x + entity.getWidth() / 2, entity.position.y);
                    //Rotate the line around itself so that it is facing the camera.
                    FastMath.rotate(rotatedLine, entity.position, entityAngle);

                    //Get the distance from the camera to the wall.
                    double dist = getDistToWall(rotatedLine, currentRay);

                    //Draw the entity if it intersects with the ray.
                    if (dist > 0) {
                        //The furthest visible wall in this slice.
                        Wall farthestWall = null;
                        //The distance to the furthest wall.
                        double farthestDistance = 0.0;

                        //If the wall buffer isn't empty, get the farthest object and distance.
                        if (!wallBuffer.get(i).isEmpty()) {
                            farthestWall = (Wall) wallBuffer.get(i).get(wallBuffer.get(i).size() - 1).object;
                            farthestDistance = wallBuffer.get(i).get(wallBuffer.get(i).size() - 1).distToCamera;
                        }

                        //If the entity isn't behind an opaque wall, add it to the list.
                        if (farthestWall == null || farthestWall.isSeeThrough() || dist <= farthestDistance) {
                            //Copy the entity's position.
                            rotatedPoint.setLocation(entity.position.x, entity.position.y);
                            //Rotate it to in front of the ray.
                            FastMath.rotate(rotatedPoint, cameraPosition, -entityAngle);
                            FastMath.subtract(rotatedPoint, cameraPosition);
                            //Recalculate the distance to the center of the entity, not where it intersects.
                            dist = rotatedPoint.y * FastMath.cos(entityAngle - cameraAngle.getValue());

                            //Create and temporarily hold an object slice before its stored in the array list.
                            ObjectSlice temp = new ObjectSlice(entity, dist, entity.getzPos(), entity.getSprite(cameraAngle.getValue()), entity.getSpritePixelData(cameraAngle.getValue()), entity.entityColor, -rotatedLine.x1 / (rotatedLine.x2 - rotatedLine.x1));

                            //If the array list is empty, add the object slice.
                            if (visibleEntities.isEmpty())
                                visibleEntities.add(temp);
                                //Otherwise, insert the object slice in order by distance.
                            else
                                for (int j = 0; j < visibleEntities.size(); j++) {
                                    if (dist < visibleEntities.get(j).distToCamera) {
                                        visibleEntities.add(j, temp);
                                        break;
                                    }
                                    //If the object slice is farther than the rest, add it to the end of the list.
                                    if (j == visibleEntities.size() - 1) {
                                        visibleEntities.add(temp);
                                        break;
                                    }
                                }
                        }
                    }
                }
            }

            //If there are no walls, just draw the entities.
            if(wallBuffer.get(i).isEmpty())
                for (ObjectSlice slice : visibleEntities)
                    drawSlice(slice, i);
            //Otherwise, draw both the entities and walls.
            else {
                //The current position in the list of see through walls.
                int j = 0;
                //An ArrayList containing the see through walls.
                ArrayList<ObjectSlice> wallList = wallBuffer.get(i);

                //Draw all of the entities and see through walls in order from nearest to farthest.
                for (ObjectSlice slice : visibleEntities) {
                    //Draw all of the see through walls in front of the entity.
                    while (j < wallList.size() && wallList.get(j).distToCamera < slice.distToCamera) {
                        drawSlice(wallList.get(j), i);
                        j++;
                    }

                    //Draw the entity.
                    drawSlice(slice, i);
                }

                //Draw the remaining see through walls behind all of the entities.
                for (; j < wallList.size() - 1; j++)
                    drawSlice(wallList.get(j), i);
            }

            //Move on to the next ray.
            currentRay += rayAngle;
        }
    }

    //Calculate the wall slices, draw the entities, and finally draw the walls.
    public void draw() {
        //Reset the pixel table so that every pixel is writable.
        clearPixelTable();

        calculateWallBuffer();
        drawEntitiesAndSeeThrough();
        drawWalls();
        drawBackground();
    }
}