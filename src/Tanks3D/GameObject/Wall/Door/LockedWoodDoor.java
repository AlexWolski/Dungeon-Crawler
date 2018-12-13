package Tanks3D.GameObject.Wall.Door;

import java.awt.*;
import java.awt.geom.Point2D;

public class LockedWoodDoor extends LockedDoor {
    private final static String lockedImage = "Locked Wood Door";
    private final static String unlockedImage = "Unlocked Wood Door";

    public LockedWoodDoor(Point2D.Double point1, Point2D.Double point2, double swingAngle, Color textureColor) {
        super(point1, point2, false, true, true, lockedImage, unlockedImage, swingAngle, null);
    }
}