package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Tank;
import Tanks3D.Utilities.Image;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public class GuidedMissileAmmo extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Pickups/Guided Missile Ammo.png");
        icon = Image.load("resources/HUD/Guided Missile.png");
    }

    public GuidedMissileAmmo(Point2D.Double position) {
        super(position, sprites, icon, null);
    }

    public void collide(Object object, ListIterator thisObject, ListIterator iterator) {
        if(object instanceof Tank) {
            //Remove the round.
            removePickup(thisObject);
        }
    }
}
