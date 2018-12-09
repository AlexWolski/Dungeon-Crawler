package Tanks3D.Object.Wall.BreakableWalls;

import Tanks3D.Object.Wall.Wall;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class BreakableWall extends Wall {
    //The textures for the wall at differing levels of damage.
    private BufferedImage[] textures;
    //The texture pixel data for the different images.
    private byte[][] texturePixelData;
    //The level of damage of the wall.
    private int damage = 0;

    //Create a new wall and a texture with the given color.
    public BreakableWall(Point2D.Double Point1, Point2D.Double Point2, BufferedImage[] textures, byte[][] texturePixelData, Color textureColor) {
        super(Point1, Point2, textures[0], textureColor);
        this.textures = textures;
        this.texturePixelData = texturePixelData;
        seeThrough = true;
    }

    public double getHeight() { return defaultWallHeight(); }

    public void breakWall() {
        //Increase the damage if it isn't already maximum.
        if(damage < textures.length) {
            //If the wall is not see-through already, make it see-through.
            if (!seeThrough)
                seeThrough = true;

            //Increase the damage on the wall.
            damage++;
            //Set the texture to the next level of damage.
            super.setTexture(textures[damage], texturePixelData[damage]);
        }
    }
}
