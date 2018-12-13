package Tanks3D.GameObject.Entity.Projectile;

import Tanks3D.GameData;
import Tanks3D.GameObject.Wall.Wall;
import Tanks3D.ObjectManager;
import Tanks3D.GameObject.Entity.Entity;
import Tanks3D.GameObject.Entity.Player;
import Tanks3D.GameObject.Wall.BreakableWalls.BreakableWall;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Projectile extends Entity {
    private final static int hitCircleRadius = 2;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.01;
    //How many rounds each pool will hold.
    private static final int poolSize = 10;
    //A pool to store projectiles before they are used.
    private static ArrayList<Projectile> bolts;
    //The damage the round does when it hits a player directly.
    private final int damage;

    //The player that shot this round.
    private Player owner;

    //The distance the round has traveled since being fired
    private double distTraveled;
    //The maximum distance the round can travel before being removed
    private static final double maxDist = 300;

    //Initialize the pools of rounds.
    public static void init() {
        //Initialize the three pools of rounds
        bolts = new ArrayList<>();

        //Populate each pool with rounds that are not visible.
        for(int i = 0; i < poolSize; i++)
            bolts.add(new CrossBowBolt(new Point2D.Double(0, 0), 0, 0, null));
    }

    public Projectile(Point2D.Double position, int zPos, double angle, int speed, int damage, BufferedImage[] sprites, Color imageColor, Player owner) {
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

    public void update(GameData data, double deltaTime) {
        //Calculate how far this round has traveled
        distTraveled += speed * deltaTime / 1000;

        //If this round has traveled too far, remove it.
        if(distTraveled > maxDist)
            this.removeRound();
        //Otherwise, update the round.
        else
            super.update(data, deltaTime);
    }

    public void collide(Object object) {
        //If the round hit a wall, cast the wall and test if it is collidable.
        if(object instanceof Wall) {
            //If the wall is a breakable wall, break the wall.
            if (object instanceof BreakableWall)
                ((BreakableWall) object).breakWall();

            //Remove the round.
            removeRound();
        }
        //If the round hits a player other than the player firing the round, damage it and remove the round.
        else if(object instanceof Player && object != owner) {
            ((Player) object).damage(damage);
            removeRound();
        }
    }

    //Add a round from the given pool to the entity list.
    private static void addFromPool(ArrayList<Projectile> projectilePool, double x, double y, double zPos, double angle, Player owner) {
        if(!projectilePool.isEmpty()) {
            //Temporarily hold the first round in the pool.
            Projectile tempProjectile = projectilePool.get(0);
            //Add the modified round to the entity list.
            ObjectManager.add(tempProjectile);
            //Remove it from the round pool.
            projectilePool.remove(0);

            //Modify the variables of the round.
            tempProjectile.position.setLocation(x, y);
            tempProjectile.zPos = zPos;
            tempProjectile.directionAngle = angle;
            tempProjectile.distTraveled = 0;
            tempProjectile.owner = owner;
        }
    }

    //Create a new round and add it to the entity list
    public static void newArmorPiercing(double x, double y, double zPos, double angle, Player owner) {
        addFromPool(bolts, x, y, zPos, angle, owner);
    }

    private void removeRound() {
        //Remove this round from the entity list.
        ObjectManager.remove(this);

        //Determine what type this round is and get the corresponding round pool.
        ArrayList<Projectile> projectilePool;

        if(this instanceof CrossBowBolt)
            projectilePool = bolts;
        else
            return;

        //Add this round back to the round pool if it isn't already in the pool.
        if(!projectilePool.contains(this))
            projectilePool.add(this);
    }
}