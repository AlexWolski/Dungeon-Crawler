package Tanks3D.Object.Entity.Round;

import Tanks3D.GameData;
import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Entity.Tank;
import Tanks3D.Object.Wall.BreakableWall;
import Tanks3D.Object.Wall.UnbreakableWall;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Round extends Entity {
    //A struct that contains the necessary data about the game.
    static private ArrayList<Entity> entityList;

    private final static int hitCircleRadius = 2;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.01;
    //How many rounds each pool will hold.
    private static final int poolSize = 10;
    //Three pools to store pre-made rounds before they are used.
    private static ArrayList<Round> ArmorPiercingPool;
    private static ArrayList<Round> GuidedMissilePool;
    private static ArrayList<Round> HighExplosivePool;
    //The damage the round does when it hits a tank directly.
    private final int damage;

    //The tank that shot this round.
    private Tank owner;

    //The distance the round has traveled since being fired
    private double distTraveled;
    //The maximum distance the round can travel before being removed
    private static final double maxDist = 300;

    //Initialize the pools of rounds.
    public static void init(ArrayList<Entity> EntityList) {
        entityList = EntityList;

        //Initialize the three pools of rounds
        ArmorPiercingPool = new ArrayList<>();
        GuidedMissilePool = new ArrayList<>();
        HighExplosivePool = new ArrayList<>();

        //Populate each pool with rounds that are not visible.
        for(int i = 0; i < poolSize; i++) {
            ArmorPiercingPool.add(new ArmorPiercing(new Point2D.Double(0, 0), 0, 0, null));
            GuidedMissilePool.add(new GuidedMissile(new Point2D.Double(0, 0), 0, 0, null));
            HighExplosivePool.add(new HighExplosive(new Point2D.Double(0, 0), 0, 0, null));
        }
    }

    public Round(Point2D.Double position, int zPos, double angle, int speed, int damage, BufferedImage[] sprites, Color imageColor, Tank owner) {
        super(hitCircleRadius, position, angle, speed);
        //Set the sprites for the round.
        setSprites(sprites, (int)(sprites[0].getWidth() * scale), (int)(sprites[0].getHeight() * scale));
        //Set the color of the entity.
        entityColor = imageColor;

        super.zPos = zPos;
        this.damage = damage;
        this.owner = owner;
        distTraveled = 0;
    }

    public void update(GameData data, double deltaTime, ListIterator<Entity> thisObject) {
        //Calculate how far this round has traveled
        distTraveled += speed * deltaTime / 1000;

        //If this round has traveled too far, remove it.
        if(distTraveled > maxDist)
            this.removeRound(thisObject);
        //Otherwise, update the round.
        else
            super.update(data, deltaTime, thisObject);
    }

    public void collide(Object object, ListIterator thisObject, ListIterator collidedObject) {
        //If the round hits a breakable wall, break it.
        if(object instanceof BreakableWall) {
            ((BreakableWall) object).breakWall();
            collidedObject.remove();
            removeRound(thisObject);
        }
        //If the round hits an unbreakable wall, destroy the round.
        else if(object instanceof UnbreakableWall) {
            removeRound(thisObject);
        }
        //If the round hits a tank other than the tank firing the round, damage it and remove the round.
        else if(object instanceof Tank && object != owner) {
            ((Tank) object).damage(damage);
            removeRound(thisObject);
        }
    }

    //Add a round from the given pool to the entity list.
    public static void addFromPool(ArrayList<Round> roundPool, double x, double y, int zPos, double angle, Tank owner) {
        if(!roundPool.isEmpty()) {
            //Temporarily hold the first round in the pool and remove it.
            Round tempRound = roundPool.get(0);

            //Add the modified round to the entity list.
            entityList.add(tempRound);
            //Remove it from the round pool.
            roundPool.remove(0);

            //Modify the variables of the round.
            tempRound.position.setLocation(x, y);
            tempRound.zPos = zPos;
            tempRound.angle = angle;
            tempRound.distTraveled = 0;
            tempRound.owner = owner;
        }
    }

    //Create a new round and add it to the entity list
    public static void newArmorPiercing(double x, double y, int zPos, double angle, Tank owner) {
        addFromPool(ArmorPiercingPool, x, y, zPos, angle, owner);
    }
    public static void newGuidedMissile(double x, double y, int zPos, double angle, Tank owner) {
        addFromPool(GuidedMissilePool, x, y, zPos, angle, owner);
    }
    public static void newHighExplosive(double x, double y, int zPos, double angle, Tank owner) {
        addFromPool(HighExplosivePool, x, y, zPos, angle, owner);
    }

    protected void removeRound(ListIterator thisObject) {
        //Remove this round from the entity list.
        thisObject.remove();

        //Determine what type this round is and get the corresponding round pool.
        ArrayList<Round> roundPool;

        if(this instanceof ArmorPiercing)
            roundPool = ArmorPiercingPool;
        else if(this instanceof  GuidedMissile)
            roundPool = GuidedMissilePool;
        else
            roundPool = HighExplosivePool;

        //Add this round back to the round pool.
        roundPool.add(this);
    }
}
