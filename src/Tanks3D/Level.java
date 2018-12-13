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
import Tanks3D.GameObject.Wall.Door.LockedWoodDoor;

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
        if(levelFile.equals("Level 1.txt")) {
            playerSpawn = new SpawnPoint(new Point2D.Double(0, 0), 0);

            floorColor = new Color(80, 40, 0).getRGB();
            ceilColor = Color.BLACK.getRGB();

            //Cell
            ObjectManager.add(new Wall(new Point2D.Double(-110, 20), new Point2D.Double(-60, 20), true, true, true, "Fence", null));
            ObjectManager.add(new LockedFenceDoor(new Point2D.Double(-60, 20), new Point2D.Double(-40, 20), 110, null));
            ObjectManager.add(new Wall(new Point2D.Double(-40, 20), new Point2D.Double(40, 20), true, true, true, "Fence", null));
            ObjectManager.add(new Wall(new Point2D.Double(40, 20), new Point2D.Double(10, -20), false, true, true, "Dirt 2", null));
            ObjectManager.add(new Wall(new Point2D.Double(10, -20), new Point2D.Double(-100, -20), false, true, true, "Dirt 2", null));
            ObjectManager.add(new Wall(new Point2D.Double(-100, -20), new Point2D.Double(-120, 0), false, true, true, "Dirt 2", null));
            ObjectManager.add(new Wall(new Point2D.Double(-120, 0), new Point2D.Double(-110, 20), false, true, true, "Dirt 2", null));

            //Cell Key
            ObjectManager.add(new Key(new Point2D.Double(-110, 0)));

            //Hallway
            ObjectManager.add(new Wall(new Point2D.Double(-110, 20), new Point2D.Double(-110, 40), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(-110, 40), new Point2D.Double(-70, 70), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(-70, 70), new Point2D.Double(60, 70), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(60, 70), new Point2D.Double(90, 30), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(90, 30), new Point2D.Double(90, -90), false, true, true, "Dirt 1", null));
            //Exit
            ObjectManager.add(new Stairs(new Point2D.Double(90, -90), new Point2D.Double(70, -90), "Dirt Stairs", null));
            ObjectManager.add(new Wall(new Point2D.Double(70, -90), new Point2D.Double(70, -40), false, true, true, "Dirt 1", null));
            //Locked door
            ObjectManager.add(new LockedWoodDoor(new Point2D.Double(70, -40), new Point2D.Double(90, -40), 110, null));
            ObjectManager.add(new Wall(new Point2D.Double(70, -40), new Point2D.Double(50, -40), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(50, -40), new Point2D.Double(30, -60), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(30, -60), new Point2D.Double(30, -100), false, true, true, "Dirt 1", null));

            ObjectManager.add(new Wall(new Point2D.Double(40, 20), new Point2D.Double(40, 0), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(40, 0), new Point2D.Double(0, -60), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(0, -60), new Point2D.Double(0, -90), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(0, -90), new Point2D.Double(-20, -80), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(-20, -80), new Point2D.Double(-40, -110), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(-40, -110), new Point2D.Double(0, -120), false, true, true, "Dirt 1", null));
            ObjectManager.add(new Wall(new Point2D.Double(0, -120), new Point2D.Double(30, -100), false, true, true, "Dirt 1", null));

            //Exit Key
            ObjectManager.add(new Key(new Point2D.Double(-20, -90)));

            mapCenter = new Point2D.Double(0, 0);
            mapWidth = 300;
            mapHeight = 300;
        }
        else if(levelFile.equals("Level 2.txt")) {
            playerSpawn = new SpawnPoint(new Point2D.Double(0, 0), 0);

            floorColor = new Color(80, 40, 0).getRGB();
            ceilColor = Color.BLACK.getRGB();


            mapCenter = new Point2D.Double(0, 0);
            mapWidth = 300;
            mapHeight = 300;
        }
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