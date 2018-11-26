package Tanks3D.Object.Entity.Round;

import Tanks3D.Object.Entity.Tank;
import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public class HighExplosive extends Round {
    private final static BufferedImage[] sprites;
    private final static Color imageColor = Color.white;
    private final static int speed = 40;
    private final static int damage = 25;
    //The radius and damage
    private final static int explosionSize = 5;
    private final static int splashDamage = 25;

    //Load the images for the round.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Rounds/Armor Piercing.png");
    }

    public HighExplosive(Point2D.Double position, int zPos, double angle, Tank owner) {
        super(position, zPos, angle, speed, damage, sprites, imageColor, owner);
    }

    //If the high explosive round hits an object, deal splash damage and remove itself.
    protected void removeRound(ListIterator thisObject) {
        //TODO splash damage
        super.removeRound(thisObject);
    }
}
