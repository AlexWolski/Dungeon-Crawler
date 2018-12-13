package Tanks3D.GameObject.Entity.Pickup;

import Tanks3D.GameObject.Entity.Player;
import Tanks3D.ObjectManager;
import Tanks3D.SoundManager;
import Tanks3D.Utilities.Image;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Key extends Pickup {
    private final static BufferedImage[] sprites;
    private final static BufferedImage icon;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.5;

    //Load the images for the pickup.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Pickups/Key.png");
        icon = Image.load("resources/Minimap Icons/Key.png");
    }

    public Key(Point2D.Double position) {
        super(position, sprites, icon, null, scale);
    }

    public void collide(Object object) {
        if(object instanceof Player)
            use((Player) object);
    }

    public void use(Player player) {
        //Play the key sound effect.
        SoundManager.playSound("Key");
        //Remove the key pickup.
        ObjectManager.remove(this);
        //Give the player a key.
        player.addItem(new Tanks3D.Item.Key());
    }
}