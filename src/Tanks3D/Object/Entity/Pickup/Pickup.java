package Tanks3D.Object.Entity.Pickup;

import Tanks3D.Object.Entity.Entity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class Pickup extends Entity {
    public Pickup(Point2D.Double position, BufferedImage[] sprites, BufferedImage icon, Color imageColor, double spriteScale) {
        //Construct the entity with no directionAngle or speed. Use the size of the image for the hitcircle size.
        super((int)(sprites[0].getWidth() * spriteScale / 2), position, 0, 0);
        //Set the images of the sprites.
        super.setSprites(sprites, (int)(sprites[0].getWidth() * spriteScale), (int)(sprites[0].getHeight() * spriteScale));
        super.setIcon(icon);
        entityColor = imageColor;

        super.zPos = (int)getHeight()/2;
    }
}