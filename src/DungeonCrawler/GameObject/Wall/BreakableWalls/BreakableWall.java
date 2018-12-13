package DungeonCrawler.GameObject.Wall.BreakableWalls;

import DungeonCrawler.GameObject.Wall.Wall;
import DungeonCrawler.SoundManager;
import DungeonCrawler.Utilities.FastMath;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class BreakableWall extends Wall {
    //The textures for the wall at differing levels of damage.
    private final String[] textureNames;
    //A list of sound effects to play when the wall is hit.
    private final String[] sounds;
    //The level of damage of the wall.
    protected int damage = 0;

    //Create a new wall and a texture with the given color.
    public BreakableWall(Point2D.Double Point1, Point2D.Double Point2, String[] textureNames, String[] sounds, Color textureColor) {
        super(Point1, Point2, true, true, true, textureNames[0], textureColor);
        this.textureNames = textureNames;
        this.sounds = sounds;
    }

    public void breakWall() {
        //Increase the damage if it isn't already maximum.
        if(damage < textureNames.length) {
            //If the wall is not see-through already, make it see-through.
            if (!seeThrough)
                seeThrough = true;

            //Increase the damage on the wall.
            damage++;
            //Set the texture to the next level of damage.
            super.setTexture(textureNames[damage]);

            //Play a random sound for the wall taking damage.
            SoundManager.playSound(sounds[FastMath.random(0, sounds.length - 1)]);

            //If the wall is at maximum damage, disable collisions.
            if(damage == textureNames.length  -1) {
                characterCollidable = false;
                projectileCollidable = false;
            }
        }
    }
}