package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Tank;
import Tanks3D.Utilities.Image;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public class Health extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;
    //The amount of health this pickup restores.
    private static final int health = 50;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Pickups/Health Crate.png");
        icon = Image.load("resources/HUD/Health.png");
    }

    public Health(Point2D.Double position) {
        super(position, sprites, icon, null);
    }

    public void collide(Object object, ListIterator thisObject, ListIterator iterator) {
        if(object instanceof Tank) {
            //Repair the tank.
            ((Tank) object).repair(health);

            //Remove the round.
            removePickup(thisObject);
        }
    }
}
