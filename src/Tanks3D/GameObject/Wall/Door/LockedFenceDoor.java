package Tanks3D.GameObject.Wall.Door;

import java.awt.*;
import java.awt.geom.Point2D;

public class LockedFenceDoor extends LockedDoor {
    private static String lockedImage = "Locked Wood Door";
    private static String unlockedImage ="Unlocked Wood Door";

    public LockedFenceDoor(Point2D.Double point1, Point2D.Double point2, Color textureColor) {
        super(point1, point2, false, true, true, lockedImage, null);
    }
}
