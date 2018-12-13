package Tanks3D;

import Tanks3D.GameObject.Entity.Pickup.CrossbowPickup;
import Tanks3D.GameObject.Entity.Pickup.Health;
import Tanks3D.GameObject.Entity.Pickup.Key;
import Tanks3D.GameObject.SpawnPoint;
import Tanks3D.GameObject.Wall.*;
import Tanks3D.GameObject.Wall.BreakableWalls.BlockedHole;
import Tanks3D.GameObject.Wall.BreakableWalls.CrackedBrick;
import Tanks3D.GameObject.Wall.Door.Door;
import Tanks3D.GameObject.Wall.Door.LockedFenceDoor;

import java.awt.*;
import java.awt.geom.Point2D;

//A 'struct' that loads the map data from a file and stores it.
public final class Level {
    private Point2D.Double mapCenter;
    private double mapWidth;
    private double mapHeight;
    private SpawnPoint playerSpawn;
    private int floorColor;
    private int ceilColor;

    //Read the data file and translate it into objects. Save the playerController spawn-points to construct the playerController objects.
    public Level(String levelFile) {
        //Read the data file and create all of the map objects.
        parseDataFile(levelFile);
    }

    //Read the json objects from the file and create them. Walls are stored in the 'wallObjects' ArrayList.
    private void parseDataFile(String levelFile) {
        playerSpawn = new SpawnPoint(new Point2D.Double(0, -80), 0);

        floorColor = Color.darkGray.getRGB();
        ceilColor = Color.darkGray.darker().getRGB();

        //Map border
        ObjectManager.add(new BlockedHole(new Point2D.Double(-100, 0), new Point2D.Double(-50, 86.6025403784), null));
        ObjectManager.add(new Stairs(new Point2D.Double(-50, 86.6025403784), new Point2D.Double(50, 86.6025403784), "Dirt Stairs", null));
        ObjectManager.add(new Door(new Point2D.Double(50, 86.6025403784), new Point2D.Double(100, 0), false, true, true, "Locked Wood Door", null));
        ObjectManager.add(new Wall(new Point2D.Double(100, 0), new Point2D.Double(50, -86.6025403784), false, true, true, "Dirt 1", null));
        ObjectManager.add(new Wall(new Point2D.Double(50, -86.6025403784), new Point2D.Double(-50, -86.6025403784), false, true, true, "Dirt 2", null));
        ObjectManager.add(new LockedFenceDoor(new Point2D.Double(-50, -86.6025403784), new Point2D.Double(-100, 0), null));

        //Breakable walls
        ObjectManager.add(new BlockedHole(new Point2D.Double(-50, 86.6025403784), new Point2D.Double(0, 0), null));
        ObjectManager.add(new CrackedBrick(new Point2D.Double(50, 86.6025403784), new Point2D.Double(0, 0), null));
        ObjectManager.add(new CrackedBrick(new Point2D.Double(100, 0), new Point2D.Double(0, 0), null));
        ObjectManager.add(new CrackedBrick(new Point2D.Double(50, -86.6025403784), new Point2D.Double(0, 0), null));
        ObjectManager.add(new CrackedBrick(new Point2D.Double(-50, -86.6025403784), new Point2D.Double(0, 0), null));
        ObjectManager.add(new CrackedBrick(new Point2D.Double(-100, 0), new Point2D.Double(0, 0), null));

        //Pickups
        ObjectManager.add(new CrossbowPickup(new Point2D.Double(0, -40)));
        ObjectManager.add(new Key(new Point2D.Double(-50, 43.3012701892)));
        ObjectManager.add(new CrossbowPickup(new Point2D.Double(50, 43.3012701892)));
        ObjectManager.add(new Health(new Point2D.Double(-50, -43.3012701892)));
        ObjectManager.add(new Health(new Point2D.Double(50, -43.3012701892)));

        mapCenter = new Point2D.Double(0, 0);
        mapWidth = 200;
        mapHeight = 200;
    }

    public double getMapCenterX() { return mapCenter.x; }
    public double getMapCenterY() { return mapCenter.y; }
    public double getMapWidth() { return mapWidth; }
    public double getMapHeight() { return mapHeight; }
    public int getFloorColor() {
        return floorColor;
    }
    public int getCeilColor() {
        return ceilColor;
    }

    public SpawnPoint getPlayerSpawn() {
        return playerSpawn;
    }
}