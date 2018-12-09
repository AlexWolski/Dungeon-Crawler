package Tanks3D.Object.Wall.BreakableWalls;

import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class BreakableBrick extends BreakableWall {
    private static BufferedImage[] textures;

    //When the game starts, load the default texture once.
    static {
        textures = new BufferedImage[4];
        //textures[0] = Image.load("resources/Textures/Breakable Brick/Brick Damage 2.png");
        textures[0] = Image.load("resources/Textures/Breakable Brick/Brick Damage 2.png");
        textures[1] = null;
        textures[2] = null;
        textures[3] = null;
    }

    public BreakableBrick(Point2D.Double Point1, Point2D.Double Point2, Color textureColor) {
        super(Point1, Point2, textures, textureColor);
    }
}