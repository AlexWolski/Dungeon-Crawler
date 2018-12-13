package DungeonCrawler.DisplayComponents;

import DungeonCrawler.GameData;
import DungeonCrawler.GameObject.Entity.Entity;
import DungeonCrawler.GameObject.Entity.Player;
import DungeonCrawler.GameObject.Wall.Stairs;
import DungeonCrawler.GameObject.Wall.Wall;
import DungeonCrawler.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Displays the minimap.
public class Minimap {
    private final GameData gameData;
    private final BufferedImage canvas;
    private final BufferedImage tempImage;
    //The raw, live pixel data of the canvas.
    private int[] tempPixelData;
    private double mapSizeRatio;
    private int backgroundColor;

    public Minimap(GameData gameData, BufferedImage canvas, Dimension dimension) {
        this.gameData = gameData;
        this.canvas = canvas;
        tempImage =  new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);

        //Get the pixel data from the canvas.
        tempPixelData = Image.getRGBColorData(tempImage);
        backgroundColor = Color.darkGray.getRGB();

        //Determine how the game world should be mapped to the minimap based on their sizes.
        if(gameData.gameLevel.getMapWidth() > gameData.gameLevel.getMapHeight()) {
            if (tempImage.getWidth() > tempImage.getHeight())
                mapSizeRatio = gameData.gameLevel.getMapWidth() / (tempImage.getHeight() - 1);
            else
                mapSizeRatio = gameData.gameLevel.getMapWidth() / (tempImage.getWidth() - 1);
        }
        else
        {
            if (tempImage.getWidth() > tempImage.getHeight())
                mapSizeRatio = gameData.gameLevel.getMapHeight() / (tempImage.getHeight() - 1);
            else
                mapSizeRatio = gameData.gameLevel.getMapHeight() / (tempImage.getWidth() - 1);
        }
    }

    //Transform a point from the map dimensions to the minimap dimensions.
    private Point2D.Double gameToMiniMap(Point2D.Double coordinate) {
        return new Point2D.Double((coordinate.x - gameData.gameLevel.getMapCenterX())/ mapSizeRatio + tempImage.getWidth()/2.0,
                                  (coordinate.y - gameData.gameLevel.getMapCenterY())/ mapSizeRatio + tempImage.getHeight()/2.0);
    }

    //Get the two points of a wall, convert them to ints, and draw them.
    private void drawWall(Graphics2D graphic, Wall wall) {
        //Map the points in the game world to points on the minimap.
        Point2D.Double p1 = gameToMiniMap(wall.getPoint1());
        Point2D.Double p2 = gameToMiniMap(wall.getPoint2());

        //Draw the points. The y coordinates are subtracted from the canvas height because the y axis is inverse.
        graphic.drawLine((int)p1.x, tempImage.getHeight()-(int)p1.y-1, (int)p2.x, tempImage.getHeight()-(int)p2.y-1);
    }

    //Rotate the icons for the entities and draw them.
    private void drawEntities(Graphics2D graphic) {
        //The size of the icon on the screen
        int iconSize;
        //The position of the entity.
        Point2D.Double entityPos;

        for(Entity entity : gameData.entityList) {
            iconSize = (int) (entity.getHitCircleRadius()/gameData.gameLevel.getMapWidth() * 2 * tempImage.getWidth());

            //Get the position of the entity.
            entityPos = gameToMiniMap(entity.position);
            //Center it by subtracting half of the image size.
            entityPos.x -= iconSize / 2.0;
            entityPos.y += iconSize / 2.0;

            //The angle that the entity is facing.
            double angle;

            //If the entity is a player, get the direction the player is viewing.
            if(entity instanceof Player)
                angle = ((Player) entity).getViewAngle().getValue();
            //Otherwise, get the direction the entity is moving.
            else
                angle = entity.directionAngle;

            //Draw the entity rotated.
            Image.drawRotated(graphic, entity.getIcon(), angle, (int) entityPos.x, tempImage.getHeight() - (int) entityPos.y, iconSize, iconSize);
        }
    }

    public void draw() {
        //Draw a background.
        for(int i = 0; i < tempImage.getWidth(); i++)
            for (int j = 0; j < tempImage.getHeight(); j++)
                Image.setRGBPixel(tempPixelData, tempImage.getWidth(), i, j, backgroundColor);

        //Create a graphic so that shapes can be drawn to the buffered image.
        Graphics2D graphic = tempImage.createGraphics();

        //Draw the walls if they are visible.
        for(Wall wall : gameData.wallList)
            if(wall.getVisible()) {
                if(wall instanceof Stairs)
                    graphic.setColor(Color.GREEN);
                else if(wall.isCharacterCollidable())
                    graphic.setColor(Color.white);
                else if(!wall.isCharacterCollidable())
                    graphic.setColor(Color.GRAY);

                drawWall(graphic, wall);
            }

        //Draw the player and entities.
        drawEntities(graphic);

        //Draw the minimap onto the canvas.
        graphic = canvas.createGraphics();
        graphic.drawImage(tempImage, canvas.getWidth() - tempImage.getWidth(), 0, null);
    }
}