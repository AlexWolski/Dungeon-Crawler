package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Entity.Player;
import Tanks3D.ObjectManager;
import Tanks3D.SoundManager;
import Tanks3D.Weapon.Crossbow;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CrossbowPickup extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.06;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Entity.getSpriteImage("Crossbow");
        icon = Entity.getIconImage("Crossbow");
    }

    public CrossbowPickup(Point2D.Double position) {
        super(position, sprites, icon, null, scale);
    }

    public void collide(Object object) {
        if(object instanceof Player) {
            //Play the key sound effect.
            SoundManager.playSound("Crossbow");
            //Remove the key pickup.
            ObjectManager.remove(this);
            //Give the player a crossbow.
            ((Player)object).addWeapon(new Crossbow(((Player)object)));
        }
    }
}
