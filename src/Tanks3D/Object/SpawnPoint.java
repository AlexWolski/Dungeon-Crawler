package Tanks3D.Object;

import java.awt.geom.Point2D;

public class SpawnPoint {
    private Point2D.Double spawnPoint;
    private int spawnAngle;

    public SpawnPoint(Point2D.Double spawnPoint, int spawnAngle) {
        this.spawnPoint = spawnPoint;
        this.spawnAngle = spawnAngle;
    }

    public Point2D.Double getPosition() {
        return spawnPoint;
    }
    public int getAngle() {
        return spawnAngle;
    }
}