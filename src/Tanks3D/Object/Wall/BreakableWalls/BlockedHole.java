package Tanks3D.Object.Wall.BreakableWalls;

import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class BlockedHole extends BreakableWall {
    private static BufferedImage[] textures;
    private static byte[][] texturePixelData;
    private static String[] sounds;

    //When the game starts, load the default texture once.
    static {
        //Initialize the arrays used to store the images.
        textures = new BufferedImage[4];
        texturePixelData = new byte[4][];

        //Load the images for the different levels of damage.
        textures[0] = Image.load("resources/Textures/Blocked Hole/Hole Damage 0.png");
        textures[1] = Image.load("resources/Textures/Blocked Hole/Hole Damage 1.png");
        textures[2] = Image.load("resources/Textures/Blocked Hole/Hole Damage 2.png");
        textures[3] = Image.load("resources/Textures/Blocked Hole/Hole Damage 3.png");

        //Get the pixel data for each image.
        for(int i = 0; i < 4; i++)
            texturePixelData[i] = Image.getABGRColorData(textures[i]);

        //Initialize the sounds that play when the wall is hit.
        sounds = new String[] { "Wood Hit 1", "Wood Hit 2" };
    }

    public BlockedHole(Point2D.Double Point1, Point2D.Double Point2, Color textureColor) {
        super(Point1, Point2, textures, texturePixelData, sounds, textureColor);
    }
}