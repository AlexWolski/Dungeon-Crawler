package Tanks3D.GameObject.Entity.Pickup;

import Tanks3D.ObjectManager;
import Tanks3D.GameObject.Entity.Player;
import Tanks3D.SoundManager;
import Tanks3D.Utilities.Image;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Health extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.02;
    //The amount of health this pickup restores.
    private static final int health = 50;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Pickups/Health Crate.png");
        icon = Image.load("resources/Minimap Icons/Health.png");
    }

    public Health(Point2D.Double position) {
        super(position, sprites, icon, null, scale);
    }

    public void collide(Object object) {
        if(object instanceof Player) {
            //Play the health sound effect.
            SoundManager.playSound("Health");
            //Heal the player.
            ((Player) object).heal(health);
            //Remove the health pickup.
            ObjectManager.remove(this);
        }
    }
}
