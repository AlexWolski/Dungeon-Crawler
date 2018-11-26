package Tanks3D;

import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Entity.Pickup.Health;
import Tanks3D.Object.SpawnPoint;
import Tanks3D.Object.Wall.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

//A 'struct' that loads the map data from a file and stores it.
public final class Level {
    public ArrayList<Wall> wallObjects;
    private Point2D.Double mapCenter;
    private double mapWidth;
    private double mapHeight;
    private SpawnPoint player1Spawn;
    private SpawnPoint player2Spawn;
    private int floorColor;
    private int ceilColor;

    //Read the data file and translate it into objects. Save the player spawn-points to construct the player objects.
    public Level(String levelFile, ArrayList<Entity> entityList) {
        //Read the data file and create all of the map objects.
        parseDataFile(levelFile, entityList);
    }

    //Read the json objects from the file and create them. Walls are stored in the 'wallObjects' ArrayList.
    private void parseDataFile(String levelFile, ArrayList<Entity> entityList) {
        //remove
        wallObjects = new ArrayList<>();
        player1Spawn = new SpawnPoint(new Point2D.Double(0, -80), 0);
        player2Spawn = new SpawnPoint(new Point2D.Double(0, 80), 180);

        floorColor = Color.darkGray.getRGB();
        ceilColor = Color.darkGray.darker().getRGB();

        //Map border
        wallObjects.add(new UnbreakableWall(new Point2D.Double(-100, 0), new Point2D.Double(-50, 86.6025403784), Color.green.darker()));
        wallObjects.add(new UnbreakableWall(new Point2D.Double(-50, 86.6025403784), new Point2D.Double(50, 86.6025403784), Color.green.darker()));
        wallObjects.add(new UnbreakableWall(new Point2D.Double(50, 86.6025403784), new Point2D.Double(100, 0), Color.green.darker()));
        wallObjects.add(new UnbreakableWall(new Point2D.Double(100, 0), new Point2D.Double(50, -86.6025403784), Color.blue));
        wallObjects.add(new UnbreakableWall(new Point2D.Double(50, -86.6025403784), new Point2D.Double(-50, -86.6025403784), Color.blue));
        wallObjects.add(new UnbreakableWall(new Point2D.Double(-50, -86.6025403784), new Point2D.Double(-100, 0), Color.blue));

        //Breakable walls
        wallObjects.add(new BreakableWall(new Point2D.Double(-50, 86.6025403784), new Point2D.Double(0, 0), null));
        wallObjects.add(new BreakableWall(new Point2D.Double(50, 86.6025403784), new Point2D.Double(0, 0), null));
        wallObjects.add(new BreakableWall(new Point2D.Double(100, 0), new Point2D.Double(0, 0), null));
        wallObjects.add(new BreakableWall(new Point2D.Double(50, -86.6025403784), new Point2D.Double(0, 0), null));
        wallObjects.add(new BreakableWall(new Point2D.Double(-50, -86.6025403784), new Point2D.Double(0, 0), null));
        wallObjects.add(new BreakableWall(new Point2D.Double(-100, 0), new Point2D.Double(0, 0), null));

        //Health crates
        entityList.add(new Health(new Point2D.Double(-50, 43.3012701892)));
        entityList.add(new Health(new Point2D.Double(50, 43.3012701892)));
        entityList.add(new Health(new Point2D.Double(-50, -43.3012701892)));
        entityList.add(new Health(new Point2D.Double(50, -43.3012701892)));

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

    public SpawnPoint getPlayer1Spawn() {
        return player1Spawn;
    }
    public SpawnPoint getPlayer2Spawn() {
        return player2Spawn;
    }
}