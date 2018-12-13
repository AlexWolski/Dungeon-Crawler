package Tanks3D.GameObject.Wall.Door;

import Tanks3D.GameObject.Usable;
import Tanks3D.GameObject.Wall.Wall;

import java.awt.*;
import java.awt.geom.Point2D;

public class Door extends Wall implements Usable {
    private final static double openSpeed = 2.0;
    private final static double maxAngle = 85.0;

    public Door(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String textureName, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, textureName, textureColor);
    }

    public void use() {
        System.out.println("Door used!!!");
    }
}
