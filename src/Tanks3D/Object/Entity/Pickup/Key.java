package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Player;
import Tanks3D.Utilities.Image;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Key extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Pickups/Key.png");
        icon = Image.load("resources/HUD/Health.png");
    }

    public Key(Point2D.Double position) {
        super(position, sprites, icon, null);
    }

    public void collide(Object object) {
        if(object instanceof Player) {

        }
    }
}