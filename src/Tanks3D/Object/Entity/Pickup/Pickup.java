package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public abstract class Pickup extends Entity {
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.02;

    public Pickup(Point2D.Double position, BufferedImage[] sprites, BufferedImage icon, Color imageColor) {
        //Construct the entity with no angle or speed. Use the size of the image for the hitcircle size.
        super((int)(sprites[0].getWidth() * scale / 2), position, 0, 0);
        //Set the images of the sprites.
        super.setSprites(sprites, (int)(sprites[0].getWidth() * scale), (int)(sprites[0].getHeight() * scale));
        super.setIcon(icon);
        entityColor = imageColor;

        super.zPos = (int)getHeight()/2;
    }

    protected void removePickup(ListIterator thisObject) {
        thisObject.remove();
        visible = false;
    }
}
