package Tanks3D.Object.Entity;

import Tanks3D.GameData;
import Tanks3D.Object.Entity.Round.Round;
import Tanks3D.Object.Wall.*;
import Tanks3D.Utilities.FastMath;
import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ListIterator;

public class Tank extends Entity {
    //How many units the tank can move per second.
    public final static double maxSpeed = 40;
    //How many degrees the tank can rotate per second.
    public final static double maxRotationSpeed = 180;
    public double rotationSpeed;

    //The tank's position and angle when it is spawned.
    private final Point2D.Double spawnPoint;
    private final double spawnAngle;
    //The default color of the tank.
    private final Color defaultColor;

    //Images for the tank in game and on the minimap.
    private final static BufferedImage[] sprites;
    private final static BufferedImage deadIcon;
    private final BufferedImage aliveIcon;

    //The number of milliseconds it takes to respawn.
    private final static int respawnCooldown = 2000;
    //The time when the tank started to respawn.
    private long respawnStartTime;
    //Determines if the player is dead, alive, or re-spawning.
    private boolean alive;
    private boolean respawning;

    //The number of milliseconds it takes before the tank can fire again.
    private static final int shotCooldown = 1000;
    //The last time the tank fired.
    private long shotTime;
    //Determines if the tank can fire again or not.
    private boolean reloading;

    //The size of the hit circle around the tank.
    private final static int hitCircleRadius = 10;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.04;
    //How high above the center tank the bullets are fired from.
    private final int gunHeight;
    //Stats of the tank.
    private final static int maxHealth = 100;
    private int health = maxHealth;
    private final static int maxLives = 3;
    private int lives = maxLives;

    //Load the images for the tank.
    static {
        //Load the sprites.
        sprites = new BufferedImage[4];
        sprites[0] = Image.load("resources/Tank/Front.png");
        sprites[1] = Image.load("resources/Tank/Left.png");
        sprites[2] = Image.load("resources/Tank/Back.png");
        sprites[3] = Image.load("resources/Tank/Right.png");

        //Load the default icon.
        deadIcon = Image.load("resources/HUD/Tank Icon.png");
    }

    public Tank(Point2D.Double spawnPoint, double spawnAngle, Color tankColor) {
        super(hitCircleRadius, new Point2D.Double(spawnPoint.x, spawnPoint.y), spawnAngle, 0);
        this.spawnPoint = spawnPoint;
        this.spawnAngle = spawnAngle;
        this.rotationSpeed = 0;
        this.defaultColor = tankColor;

        //The tank is alive.
        alive = true;
        respawning = false;

        //Set the current color to the default color.
        tankColor = new Color(defaultColor.getRGB());
        entityColor = tankColor;

        //Pass the sprites to the parent class.
        setSprites(sprites, (int)(sprites[0].getWidth() * scale), (int)(sprites[0].getHeight() * scale));

        //Copy and color it to get the alive icon.
        aliveIcon = Image.copy(deadIcon);
        Image.tintImage(aliveIcon, tankColor);
        //Set the icon to the alive icon.
        setIcon(aliveIcon);

        //Set the height of the tank.
        zPos = (int)getHeight()/2;
        //Set the height of the gun.
        gunHeight = zPos + (int)(getHeight()/2 * 0.65);
    }

    public void update(GameData data, double deltaTime, ListIterator<Entity> thisObject) {
        //If the tank has no health and isn't dead yet, kill it.
        if(alive && health <= 0)
            die();
        else {
            //Update the angle and position of the tank.
            angle += rotationSpeed * deltaTime / 1000;
            angle = FastMath.formatAngle(angle);
            super.update(data, deltaTime, thisObject);
        }
    }

    public void collide(Object object, ListIterator thisObject, ListIterator collidedObject) {
        //If the tank collides with a breakable wall, destroy the wall.
        if(object instanceof BreakableWall) {
            ((BreakableWall)object).breakWall();
            collidedObject.remove();
        }
        //If the tank hits the corner of the wall, fix its position.
        else if(object instanceof Point2D.Double) {
            Point2D.Double point = (Point2D.Double)object;

            //Calculate the angle between the tank and the hit circle.
            double angle = Math.toDegrees(Math.atan2(point.x - position.x, point.y - position.y));

            //Move the tank.
            this.position.x = point.x - hitCircleRadius * FastMath.sin(angle);
            this.position.y = point.y - hitCircleRadius * FastMath.cos(angle);
        }
        //If the tank hits a wall, fix its position.
        else if(object instanceof UnbreakableWall) {
            //Get the angle of the line.
            double lineAngle = ((UnbreakableWall) object).getAngle();
            //Copy the first point of the wall.
            Point2D.Double linePoint1 = ((UnbreakableWall) object).getPoint1();

            //Rotate the point so that the line it would be vertical next to the entity.
            FastMath.rotate(linePoint1, position, -lineAngle);
            //The x distance between the line and the entity.
            double xDistance = linePoint1.x - position.x;

            if (xDistance > 0) {
                this.position.x -= (hitCircleRadius - xDistance) * FastMath.cos(lineAngle);
                this.position.y += (hitCircleRadius - xDistance) * FastMath.sin(lineAngle);
            } else {
                this.position.x += (hitCircleRadius + xDistance) * FastMath.cos(lineAngle);
                this.position.y -= (hitCircleRadius + xDistance) * FastMath.sin(lineAngle);
            }
        }
        else if(object instanceof Tank) {
            Tank tank = (Tank)object;

            //Calculate the angle between the centers of the two tanks.
            double angle = Math.toDegrees(Math.atan2(tank.position.x - position.x, tank.position.y - position.y));

            //Move this tank by half of the distance.
            this.position.x = tank.position.x -  2 * hitCircleRadius * FastMath.sin(angle);
            this.position.y = tank.position.y - 2 * hitCircleRadius * FastMath.cos(angle);
        }
    }

    public void fire() {
        //If the tank is not reloading, fire.
        if(!reloading) {
            //Set the last time the tank fired.
            shotTime = System.currentTimeMillis();
            reloading = true;

            //Calculate the distance to spawn the round.
            double distance = hitCircleRadius;
            //Calculate the x and y position to spawn the round based on the tank's position and angle.
            double xPos = position.x + distance * FastMath.sin(angle);
            double yPos = position.y + distance * FastMath.cos(angle);
            //Create the round and add it to the entity list.
            Round.newArmorPiercing(xPos, yPos, gunHeight, angle, this);
        }
        //If the tank is reloading, check if the reload time is up. If it is, set reloading to false.
        else if(System.currentTimeMillis() >= shotTime + shotCooldown) {
            reloading = false;
        }
    }

    //Deal damage to the tank.
    public void damage(int damage) {
        health -= damage;
    }

    //Repair the tank.
    public void repair(int health) {
        this.health += health;

        //If the new health is above the maximum, set it to the maximum.
        if(this.health > maxHealth)
            this.health = maxHealth;
    }

    public void die() {
        entityColor = Color.GRAY;
        alive = false;
        lives--;
        speed = 0;
        rotationSpeed = 0;
        setIcon(deadIcon);
    }

    //Respawn the tank.
    public void respawn() {
        //If the tank hasn't started re-spawning yet, start the timer.
        if(!respawning) {
            respawnStartTime = System.currentTimeMillis();
            respawning = true;
        }
        //If the tank is re-spawning, check if the respawn time is up. If it is, respawn the tank.
        else if(System.currentTimeMillis() >= respawnStartTime + respawnCooldown)
            resetTank();
    }

    public void resetTank() {
        position.setLocation(spawnPoint);
        angle = spawnAngle;
        entityColor = new Color(defaultColor.getRGB());
        health = maxHealth;
        alive = true;
        respawning = false;
        setIcon(aliveIcon);
    }

    public void resetLives() {
        lives = maxLives;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }
    public double getMaxRotationSpeed() {
        return maxRotationSpeed;
    }
    public Color getColor() {
        return entityColor;
    }
    public Point2D.Double getPosition() {
        return position;
    }
    public double getAngle() {
        return super.angle;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getHealth() {
        return health;
    }
    public int getLives() {
        return lives;
    }
    public boolean isAlive() {
        return alive;
    }
}