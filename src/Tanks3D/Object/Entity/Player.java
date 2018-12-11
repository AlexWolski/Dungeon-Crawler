package Tanks3D.Object.Entity;

import Tanks3D.GameData;
import Tanks3D.GameManager;
import Tanks3D.Object.Entity.Round.Projectile;
import Tanks3D.Object.SpawnPoint;
import Tanks3D.Object.Wall.*;
import Tanks3D.Utilities.FastMath;
import Tanks3D.Utilities.Image;
import Tanks3D.Utilities.Wrappers.MutableDouble;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    //How many units the tank can move per second.
    public final static double maxSpeed = 40;
    //How many degrees the tank can rotate per second.
    public final static double maxRotationSpeed = 180;
    public double rotationSpeed;

    //The player's position and directionAngle when it is spawned.
    private final SpawnPoint spawnPoint;
    //The angle that the player is facing.
    private final MutableDouble viewAngle;
    //The direction the player is moving relative to the camera.
    private double controlAngle;
    //The default height of the player.
    private final double height = 10;

    //Images for the tank in game and on the minimap.
    private final static BufferedImage[] sprites;
    private final static BufferedImage playerIcon;

    //Determines if the playerController is dead, alive, or re-spawning.
    private boolean alive;

    //The number of milliseconds it takes before the tank can fire again.
    private static final int shotCooldown = 100;
    //The last time the tank fired.
    private long shotTime;
    //Determines if the tank can fire again or not.
    private boolean reloading;

    //The size of the hit circle around the tank.
    private final static int hitCircleRadius = 10;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.04;
    //How high projectiles are fired.
    private final double weaponHeight;
    //Stats of the tank.
    private final static int maxHealth = 100;
    private int health = maxHealth;

    //Load the images for the tank.
    static {
        //Load a blank buffered image as a placeholder.
        sprites = new BufferedImage[] { new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB) };

        //Load the default icon.
        playerIcon = Image.load("resources/HUD/Player Icon.png");
    }

    public Player(SpawnPoint spawnPoint) {
        super(hitCircleRadius, new Point2D.Double(spawnPoint.getPosition().x, spawnPoint.getPosition().y), spawnPoint.getAngle(), 0);
        this.spawnPoint = spawnPoint;
        this.rotationSpeed = 0;

        //Set the angle that the player is facing.
        viewAngle = new MutableDouble(spawnPoint.getAngle());
        //By default, the player is moving forward.
        controlAngle = 0;

        //The player is alive.
        alive = true;
        //The player has no color.
        entityColor = null;

        //Pass the sprites to the parent class.
        setSprites(sprites, (int)(sprites[0].getWidth() * scale), (int)(sprites[0].getHeight() * scale));

        //Set the icon to the alive icon.
        Image.tintImage(playerIcon, Color.RED);
        setIcon(playerIcon);

        //Set the zPos to the default zPos.
        zPos = height;
        //Set the height of the gun.
        weaponHeight = zPos;
    }

    public void update(GameData data, double deltaTime) {
        //If the tank has no health and isn't dead yet, kill it.
        if(alive && health <= 0)
            die();
        else {
            //Update the directionAngle and position of the tank.
            viewAngle.add(rotationSpeed * deltaTime / 1000);
            viewAngle.setValue(FastMath.formatAngle(viewAngle.getValue()));
            directionAngle = FastMath.formatAngle(viewAngle.getValue() + controlAngle);
            super.update(data, deltaTime);
        }
    }

    public void collide(Object object) {
        //If the tank hits the corner of the wall, fix its position.
        if(object instanceof Point2D.Double) {
            Point2D.Double point = (Point2D.Double)object;

            //Calculate the directionAngle between the tank and the hit circle.
            double angle = Math.toDegrees(Math.atan2(point.x - position.x, point.y - position.y));

            //Move the tank.
            this.position.x = point.x - hitCircleRadius * FastMath.sin(angle);
            this.position.y = point.y - hitCircleRadius * FastMath.cos(angle);
        }
        //If the tank hits a wall, fix its position.
        else if(object instanceof Wall) {
            //Get the directionAngle of the line.
            double lineAngle = ((Wall) object).getAngle();
            //Copy the first point of the wall.
            Point2D.Double linePoint1 = ((Wall) object).getPoint1();

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
        else if(object instanceof Player) {
            Player player = (Player)object;

            //Calculate the directionAngle between the centers of the two tanks.
            double angle = Math.toDegrees(Math.atan2(player.position.x - position.x, player.position.y - position.y));

            //Move this player by half of the distance.
            this.position.x = player.position.x -  2 * hitCircleRadius * FastMath.sin(angle);
            this.position.y = player.position.y - 2 * hitCircleRadius * FastMath.cos(angle);
        }
    }

    public void fire() {
        //If the tank is not reloading, fire.
        if(!reloading) {
            //Set the last time the tank fired.
            shotTime = System.currentTimeMillis();
            reloading = true;

            //Calculate the distance to spawn the round.
            double distance = 5;
            //Calculate the x and y position to spawn the round based on the tank's position and directionAngle.
            double xPos = position.x + distance * FastMath.sin(viewAngle.getValue());
            double yPos = position.y + distance * FastMath.cos(viewAngle.getValue());
            //Create the round and add it to the entity list.
            Projectile.newArmorPiercing(xPos, yPos, weaponHeight, viewAngle.getValue(), this);
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
    public void heal(int health) {
        this.health += health;

        //If the new health is above the maximum, set it to the maximum.
        if(this.health > maxHealth)
            this.health = maxHealth;
    }

    public void die() {
        alive = false;
        speed = 0;
        rotationSpeed = 0;

        GameManager.endGame(false);
    }

    public void resetPlayer() {
        position.setLocation(spawnPoint.getPosition());
        viewAngle.setValue(spawnPoint.getAngle());
        health = maxHealth;
        alive = true;
    }

    public MutableDouble getViewAngle() {
        return viewAngle;
    }
    public void setViewAngle(double angle) {
        viewAngle.setValue(angle);
    }
    public void setControlAngle(double controlAngle) {
        this.controlAngle = controlAngle;
    }
    public double getMaxSpeed() {
        return maxSpeed;
    }
    public double getMaxRotationSpeed() {
        return maxRotationSpeed;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getHealth() {
        return health;
    }
    public Point2D.Double getPosition() {
        return position;
    }
    public MutableDouble getAngle() {
        return viewAngle;
    }
    public boolean isAlive() {
        return alive;
    }
}