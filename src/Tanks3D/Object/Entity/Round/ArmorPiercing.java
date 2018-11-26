package Tanks3D.Object.Entity.Round;

import Tanks3D.Object.Entity.Tank;
import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ArmorPiercing extends Round {
    private final static BufferedImage[] sprites;
    private final static Color imageColor = Color.white;
    private final static int speed = 60;
    private final static int damage = 25;

    //Load the images for the round.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Rounds/Armor Piercing.png");
    }

    public ArmorPiercing(Point2D.Double position, int zPos, double angle, Tank owner) {
        super(position, zPos, angle, speed, damage, sprites, imageColor, owner);
    }
}
