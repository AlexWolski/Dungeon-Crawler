package Tanks3D.GameObject.Wall.BreakableWalls;

import java.awt.*;
import java.awt.geom.Point2D;

public class BlockedHole extends BreakableWall {
    private static String[] textureNames;
    private static String[] sounds;

    //When the game starts, load the default texture once.
    static {
        //Populate the texture names array.
        textureNames = new String[] { "Hole Damage 0", "Hole Damage 1", "Hole Damage 2", "Hole Damage 3" };
        //Initialize the sounds that play when the wall is hit.
        sounds = new String[] { "Wood Hit 1", "Wood Hit 2" };
    }

    public BlockedHole(Point2D.Double Point1, Point2D.Double Point2, Color textureColor) {
        super(Point1, Point2, textureNames, sounds, textureColor);
    }
}