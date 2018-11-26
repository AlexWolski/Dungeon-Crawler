package Tanks3D.DisplayComponents;

import Tanks3D.GameData;
import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Wall.Wall;
import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Displays the minimap.
public class Minimap implements Runnable {
    private final GameData gameData;
    private final BufferedImage canvas;
    private double mapSizeRatio;
    private int backgroundColor;

    public Minimap(GameData gameData, BufferedImage canvas) {
        this.gameData = gameData;
        this.canvas = canvas;
        backgroundColor = Color.darkGray.getRGB();

        //Determine how the game world should be mapped to the minimap based on their sizes.
        if(gameData.gameLevel.getMapWidth() > gameData.gameLevel.getMapHeight()) {
            if (canvas.getWidth() > canvas.getHeight())
                mapSizeRatio = gameData.gameLevel.getMapWidth() / (canvas.getHeight() - 1);
            else
                mapSizeRatio = gameData.gameLevel.getMapWidth() / (canvas.getWidth() - 1);
        }
        else
        {
            if (canvas.getWidth() > canvas.getHeight())
                mapSizeRatio = gameData.gameLevel.getMapHeight() / (canvas.getHeight() - 1);
            else
                mapSizeRatio = gameData.gameLevel.getMapHeight() / (canvas.getWidth() - 1);
        }
    }

    //Transform a point from the map dimensions to the minimap dimensions.
    private Point2D.Double gameToMiniMap(Point2D.Double coordinate) {
        return new Point2D.Double((coordinate.x - gameData.gameLevel.getMapCenterX())/ mapSizeRatio + canvas.getWidth()/2.0,
                                  (coordinate.y - gameData.gameLevel.getMapCenterY())/ mapSizeRatio + canvas.getHeight()/2.0);
    }

    //Get the two points of a wall, convert them to ints, and draw them.
    private void drawWall(Graphics2D graphic, Wall wall) {
        //Map the points in the game world to points on the minimap.
        Point2D.Double p1 = gameToMiniMap(wall.getPoint1());
        Point2D.Double p2 = gameToMiniMap(wall.getPoint2());

        //Draw the points. The y coordinates are subtracted from the canvas height because the y axis is inverse.
        graphic.drawLine((int)p1.x, canvas.getHeight()-(int)p1.y-1, (int)p2.x, canvas.getHeight()-(int)p2.y-1);
    }

    //Rotate the icons for the entities and draw them.
    private void drawEntities(Graphics2D graphic) {
        //The size of the icon on the screen
        int iconSize;
        //The position of the entity.
        Point2D.Double entityPos;

        for(Entity entity : gameData.entityList) {
            iconSize = (int) (entity.getHitCircleRadius()/gameData.gameLevel.getMapWidth() * 2 * canvas.getWidth());

            //Get the position of the first tank.
            entityPos = gameToMiniMap(entity.position);
            //Center it by subtracting half of the image size.
            entityPos.x -= iconSize / 2.0;
            entityPos.y += iconSize / 2.0;
            //Draw it rotated.
            Image.drawRotated(graphic, entity.getIcon(), entity.angle, (int) entityPos.x, canvas.getHeight() - (int) entityPos.y, iconSize, iconSize);
        }
    }

    public void draw() {
        //Draw a background.
        for(int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++)
                canvas.setRGB(i, j, backgroundColor);
        }

        //Create a graphic so that shapes can be drawn to the buffered image.
        Graphics2D graphic = canvas.createGraphics();

        //Draw the walls if they are visible.

        for(Wall wall : gameData.gameLevel.wallObjects)
            if(wall.getVisible())
                drawWall(graphic, wall);

        //Draw the tanks.
        drawEntities(graphic);

        //Delete the graphics object.
        graphic.dispose();
    }

    public void run() {
        draw();
    }
}