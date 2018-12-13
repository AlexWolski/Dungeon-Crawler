package Tanks3D.GameObject.Wall.Door;

import Tanks3D.GameObject.Entity.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class LockedDoor extends Door {
    private String unlockedImage;

    protected boolean locked;

    public LockedDoor(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String lockedImage, String unlockedImage, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, lockedImage, textureColor);

        this.unlockedImage = unlockedImage;

        locked = true;
    }

    public void use(Player player) {
        if(locked) {
            if(player.getKey() != null) {
                locked = false;
                setTexture(unlockedImage);
            }
        }
        else
            super.use(player);
    }
}
