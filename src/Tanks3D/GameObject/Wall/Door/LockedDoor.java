package Tanks3D.GameObject.Wall.Door;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class LockedDoor extends Door {
    public LockedDoor(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String textureName, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, textureName, textureColor);
    }

    public void use() {

    }
}
