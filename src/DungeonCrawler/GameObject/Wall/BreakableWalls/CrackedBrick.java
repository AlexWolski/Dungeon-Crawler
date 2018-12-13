package DungeonCrawler.GameObject.Wall.BreakableWalls;

import DungeonCrawler.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;

public class CrackedBrick extends BreakableWall {
    private static String[] textureNames;
    private static String[] sounds;

    //When the game starts, load the default texture once.
    static {
        //Populate the texture names array.
        textureNames = new String[] { "Brick Damage 0", "Brick Damage 1", "Brick Damage 2", "Brick Damage 3" };
        //Initialize the sounds that play when the wall is hit.
        sounds = new String[] { "Wall Hit" };
    }

    public CrackedBrick(Point2D.Double Point1, Point2D.Double Point2, Color textureColor) {
        super(Point1, Point2, textureNames, sounds, textureColor);
    }

    public void breakWall() {
        super.breakWall();

        //If the wall was broken, play the crumble sound.
        if(damage == textureNames.length - 1)
            SoundManager.playSound("Wall Crumble");
    }
}