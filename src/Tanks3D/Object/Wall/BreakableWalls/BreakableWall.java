package Tanks3D.Object.Wall.BreakableWalls;

import Tanks3D.Object.Wall.Wall;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class BreakableWall extends Wall {
    //The textures for the wall at differing levels of damage.
    private BufferedImage[] textures;
    //The level of damage of the wall.
    private int damage = 0;

    //Create a new wall and a texture with the given color.
    public BreakableWall(Point2D.Double Point1, Point2D.Double Point2, BufferedImage[] textures, Color textureColor) {
        super(Point1, Point2, textures[0], textureColor);
        this.textures = textures;
        seeThrough = true;
    }

    public double getHeight() { return defaultWallHeight(); }
    public void breakWall() {
        damage++;

        if(damage < textures.length)
            super.setTexture(textures[damage]);
    }
}
