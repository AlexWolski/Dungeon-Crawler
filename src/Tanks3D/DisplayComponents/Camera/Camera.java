package Tanks3D.DisplayComponents.Camera;

import Tanks3D.GameData;
import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Wall.Wall;
import Tanks3D.Utilities.FastMath;
import Tanks3D.Utilities.Image;
import Tanks3D.Utilities.Wrappers.MutableDouble;

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
    //The raw, live pixel data of the canvas.
    private int[] canvasPixelData;
    //An array containing the wall slices that need to be drawn.
    private final ObjectSlice[] wallBuffer;
    //An array full of booleans indicating whether a pixel has been written to or not.
    private Boolean[][] pixelTable;
    //The horizontal and vertical field of view of the camera.
    private final static double FOV = 60;
    //The cameraPosition of the camera.
    private final Point2D.Double cameraPosition;
    //The angle of the camera.
    private final MutableDouble cameraAngle;
    //The distance from the camera to the projection plane.
    private final double distProjectionPlane;
    //The color of the floor and ceiling.
    private final int floorColor, ceilColor;

    public Camera(GameData gameData, BufferedImage canvas, Point2D.Double position, MutableDouble angle) {
        this.canvas = canvas;
        this.gameData = gameData;

        //Get the pixel data from the canvas.
        canvasPixelData = Image.getRGBColorData(canvas);
        //Calculate the distance from the camera to the projection plane from the FOV and image buffer width.
        distProjectionPlane = FastMath.cos(FOV /2) / FastMath.sin(FOV /2) * canvas.getWidth() / 2;
        //Initialize the array of wall slices and array of booleans.
        wallBuffer = new ObjectSlice[canvas.getWidth()];
        pixelTable = new Boolean[canvas.getWidth()][canvas.getHeight()];
        //Get the floor and ceiling color.
        floorColor = gameData.gameLevel.getFloorColor();
        ceilColor = gameData.gameLevel.getCeilColor();

        //Fill the pixel table so that it is all writable.
        clearPixelTable();

        //Store the cameraPosition and angle of the player.
        this.cameraPosition = position;
        this.cameraAngle = angle;
    }

    //Reset the pixel table.
    private void clearPixelTable() {
        for(int i = 0; i < canvas.getWidth(); i++)
            for(int j = 0; j < canvas.getHeight(); j++)
                pixelTable[i][j] = true;
    }

    //Draw a slice of an image.
    private void drawSlice(ObjectSlice slice, int canvasX) {
        if(slice != null) {
            //The color of the pixel being drawn to the screen.
            int pixelColor;
            //The y coordinate of the image where it will start being drawn.
            int imageStart = 0;
            //The height of the image that actually gets drawn.
            int sliceHeight = (int) (slice.object.getHeight() / slice.distToCamera * distProjectionPlane);

            //The y cameraPosition where the image will be drawn.
            int zPos = (int) Math.round(canvas.getHeight()/2.0 + ((Wall.defaultWallHeight()/slice.distToCamera * distProjectionPlane)/2) - (slice.zPos/slice.distToCamera * distProjectionPlane));

            //Calculate the y positions where the wall starts and stops on the screen.
            int wallStart = (int) (zPos - sliceHeight/2.0);
            int wallEnd = (int) (zPos + sliceHeight/2.0);

            if (wallStart < 0)
                wallStart = 0;

            if (wallEnd > canvas.getHeight())
                wallEnd = canvas.getHeight();

            //If the image wasn't loaded properly, just draw a purple slice.
            if(slice.image == null) {
                for (int canvasY = wallStart; canvasY < wallEnd; canvasY++)
                    if(pixelTable[canvasX][canvasY].equals(true)) {
                        Image.setRGBPixel(canvasPixelData, canvas.getWidth(), canvasX, canvasY, Color.MAGENTA.getRGB());
                        pixelTable[canvasX][canvasY] = false;
                    }
            }
            //Otherwise, draw the image.
            else {
                //If the slice is larger than the screen, set the image height and reset the onscreen height.
                if (sliceHeight > canvas.getHeight())
                    imageStart = (int) ((sliceHeight - canvas.getHeight()) / 2.0 / sliceHeight * slice.image.getHeight());

                //The column of the image that will be drawn.
                int imageX = (int) (slice.intersectRatio * slice.image.getWidth());
                //The row of the image that is going to be drawn.
                int imageY = 0;

                //Loop through the image and draw all of the rows.
                for (int canvasY = wallStart; canvasY < wallEnd; canvasY++) {
                    //If the pixel hasn't been written to yet, draw the pixel.
                    if(pixelTable[canvasX][canvasY].equals(true)) {
                        //Get the color of the pixel at the current point in the image if the color is valid.
                        //pixelColor = slice.image.getRGB(imageX, imageStart + (int) (imageY / (double) sliceHeight * slice.image.getHeight()));
                        pixelColor = Image.getABGRPixel(slice.imagePixelData, slice.image.getWidth(), imageX, imageStart + (int) (imageY / (double) sliceHeight * slice.image.getHeight()));

                        //If the pixel is not transparent, draw the pixel.
                        //if ((pixelColor >> 24) != 0xff) {
                            //If the color is not null, tint the pixel.
                            if(slice.imageColor != null)
                                pixelColor = Image.tintPixel(new Color(pixelColor), slice.imageColor);

                            //Draw the pixel.
                            Image.setRGBPixel(canvasPixelData, canvas.getWidth(), canvasX, canvasY, pixelColor);
                            pixelTable[canvasX][canvasY] = false;
                        //}
                    }

                    //Move on to the next row to draw.
                    imageY++;
                }
            }
        }
    }

    //Fill the given buffer with the slices of wall that needs to be drawn.
    private void calculateWallBuffer() {
        //The angle between each ray.
        double rayAngle = FOV /wallBuffer.length;
        //The angle of the first ray.
        double currentRay = -FOV /2;
        //The distance from the camera to the wall slice.dd
        double dist;

        //Variables for calculating the intersection ratio of the wall texture.
        double scaledImgWidth;
        double inGameImgWidth;

        //Iterate through each ray.
        for(int i = 0; i < wallBuffer.length; i++) {
            wallBuffer[i] = null;

            //Iterate through each wall and determine which one to draw.
            for(Wall wall : gameData.gameLevel.wallObjects) {
                //Scan the wall if it is visible.
                if(wall.getVisible()) {
                    //Copy the points of the wall.
                    Line2D.Double line = wall.getLine();

                    //Rotate the wall so that the ray is facing along the y axis.
                    FastMath.rotate(line, cameraPosition, -cameraAngle.getValue() - currentRay);
                    FastMath.translate(line, -cameraPosition.x, -cameraPosition.y);

                    //The distance between the camera and the point on the wall that the ray hit. Multiply by cos to remove the fish-eye effect.
                    dist = FastMath.getYIntercept(line) * FastMath.cos(currentRay);

                    //If this wall is visible and is closer to the camera than walls previously checked, save it in the buffer.
                    if (dist > 0 && (wallBuffer[i] == null || wallBuffer[i].distToCamera > dist)) {
                        //If the texture for the wall wasn't loaded correctly, put a dummy variable in for inGameImgWidth.
                        if(wall.getTexture() == null)
                            inGameImgWidth = -1;
                        else {
                            //The size of the image fit to the wall.
                            scaledImgWidth = wall.getHeight() / wall.getTexture().getHeight() * wall.getTexture().getWidth();
                            //The width of the image as viewed from the camera.
                            inGameImgWidth = Math.abs(line.x2 - line.x1) / (wall.getWidth()/scaledImgWidth);
                        }

                        //Calculate the intersection ratio for the texture and
                        wallBuffer[i] = new ObjectSlice(wall, dist, wall.getHeight()/2, wall.getTexture(), wall.getTexturePixelData(), wall.getTextureColor(), (Math.abs(line.x1) % inGameImgWidth)/inGameImgWidth);
                    }
                }
            }

            //Move on to the next ray.
            currentRay += rayAngle;
        }
    }

    //Draw the wall slices in the wall buffer.
    private void drawWalls() {
        //Iterate through the wall slices and draw them.
        for(int i = 0; i < canvas.getWidth(); i++) {
            drawSlice(wallBuffer[i], i);

            //Draw the ceiling.
            for(int j = 0; j < canvas.getHeight()/2; j++)
                if(pixelTable[i][j]) {
                    Image.setRGBPixel(canvasPixelData, canvas.getWidth(), i, j, ceilColor);
                    pixelTable[i][j] = false;
                }

            //Draw the floor.
            for(int j = canvas.getHeight()/2; j < canvas.getHeight(); j++)
                if(pixelTable[i][j]) {
                    Image.setRGBPixel(canvasPixelData, canvas.getWidth(), i, j, floorColor);
                    pixelTable[i][j] = false;
                }
        }
    }

    //Draw the entities, comparing their distance to the wall buffer.
    private void drawEntities() {
        //The angle between each ray.
        double rayAngle = FOV /wallBuffer.length;
        //The angle of the first ray.
        double currentRay = -FOV /2;
        //Distance from the camera to the center of the entity.
        double dist;
        //The angle between the camera and the entity.
        double entityAngle;
        //A line representing the entity's location and the width of its image.
        Line2D.Double rotatedLine = new Line2D.Double();
        //The rotated point of the entity to calculate the distance to the center of the it.
        Point2D.Double rotatedPoint = new Point2D.Double();
        //A list of entities that the ray intersects.
        ArrayList<ObjectSlice> visibleEntities = new ArrayList<>();
        //Temporarily hold an object slice before its stored in the array list.
        ObjectSlice currentSlice;

        //Iterate through each ray.
        for(int i = 0; i < wallBuffer.length; i++) {
            //Empty the list of entities.
            visibleEntities.clear();

            //Iterate through the entity list and draw the slices that are visible.
            for(Entity entity : gameData.entityList) {
                //Scan the entity if it is visible.
                if (entity.getVisible()) {
                    //The angle between the camera and the entity.
                    entityAngle = Math.toDegrees(Math.atan2(entity.position.x - cameraPosition.x, entity.position.y - cameraPosition.y));

                    //Create a line at the entity's position with the same width. It is horizontal.
                    rotatedLine.setLine(entity.position.x - entity.getWidth() / 2, entity.position.y, entity.position.x + entity.getWidth() / 2, entity.position.y);
                    //Rotate the line around itself so that it is facing the camera.
                    FastMath.rotate(rotatedLine, entity.position, entityAngle);
                    //Rotate the line around the camera so that the ray is along the y axis.
                    FastMath.rotate(rotatedLine, cameraPosition, -cameraAngle.getValue() - currentRay);
                    FastMath.translate(rotatedLine, -cameraPosition.x, -cameraPosition.y);

                    //Get the distance to the entity using the line accounting for the fish eye effect.
                    dist = FastMath.getYIntercept(rotatedLine) * FastMath.cos(currentRay);

                    //If the entity intersects with the ray and it is in front of the walls, draw it.
                    if (dist > 0 && (wallBuffer[i] == null || dist <= wallBuffer[i].distToCamera)) {
                        //Copy the entity's position.
                        rotatedPoint.setLocation(entity.position.x, entity.position.y);
                        //Rotate it to in front of the ray.
                        FastMath.rotate(rotatedPoint, cameraPosition, -entityAngle);
                        FastMath.subtract(rotatedPoint, cameraPosition);
                        //Recalculate the distance to the center of the entity, not where it intersects.
                        dist = rotatedPoint.y *  FastMath.cos(entityAngle - cameraAngle.getValue());

                        //Create the object slice.
                        currentSlice = new ObjectSlice(entity, dist, entity.getzPos(), entity.getSprite(cameraAngle.getValue()), entity.getSpritePixelData(cameraAngle.getValue()), entity.entityColor, -rotatedLine.x1 / (rotatedLine.x2 - rotatedLine.x1));

                        //If the array list is empty, add the object slice.
                        if (visibleEntities.isEmpty())
                            visibleEntities.add(currentSlice);
                            //Otherwise, insert the object slice in order by distance.
                        else
                            for (int j = 0; j < visibleEntities.size(); j++) {
                                if (dist < visibleEntities.get(j).distToCamera) {
                                    visibleEntities.add(j, currentSlice);
                                    break;
                                }
                                //If the object slice is farther than the rest, add it to the end of the list.
                                if (j == visibleEntities.size() - 1) {
                                    visibleEntities.add(currentSlice);
                                    break;
                                }
                            }
                    }
                }
            }

            //Draw all of the entities in the array list in order from nearest to farthest.
            for(ObjectSlice slice : visibleEntities)
                drawSlice(slice, i);

            //Move on to the next ray.
            currentRay += rayAngle;
        }
    }

    //Calculate the wall slices, draw the entities, and finally draw the walls.
    public void draw() {
        calculateWallBuffer();
        drawEntities();
        drawWalls();

        //Reset the pixel table so that every pixel is writable.
        clearPixelTable();
    }
}