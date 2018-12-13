package Tanks3D.GameObject.Wall.Door;

import java.awt.*;
import java.awt.geom.Point2D;

public class LockedFenceDoor extends LockedDoor {
    private static String lockedImage = "Locked Fence Door";
    private static String unlockedImage ="Unlocked Fence Door";

    public LockedFenceDoor(Point2D.Double point1, Point2D.Double point2, Color textureColor) {
        super(point1, point2, true, true, true, lockedImage, null);
    }
}