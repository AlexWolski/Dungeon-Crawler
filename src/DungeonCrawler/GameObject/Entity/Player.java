package DungeonCrawler.GameObject.Entity;

import DungeonCrawler.GameData;
import DungeonCrawler.GameManager;
import DungeonCrawler.GameObject.Update;
import DungeonCrawler.GameObject.Usable;
import DungeonCrawler.Item.Item;
import DungeonCrawler.GameObject.SpawnPoint;
import DungeonCrawler.GameObject.Wall.*;
import DungeonCrawler.Item.Key;
import DungeonCrawler.SoundManager;
import DungeonCrawler.Utilities.FastMath;
import DungeonCrawler.Utilities.Image;
import DungeonCrawler.Utilities.Wrappers.MutableDouble;
import DungeonCrawler.Weapon.Weapon;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity implements Update {
    //How many units the tank can move per second.
    public final static double maxSpeed = 40;
    //How many degrees the tank can rotate per second.
    public final static double maxRotationSpeed = 180;
    public double rotationSpeed;

    //How far the player moves before making a footstep.
    private final static double strideLength = 15;
    //The distance traveled since the last footstep.
    private double distanceSinceLastStep = 0;

    //The farthest an object can be and the player still be able to use it.
    private final static double useReach = 20;
    //The farthest an enemy can be and the player still be able to hit it.
    private final static double meleReach = 20;

    //A list of objects that the player can interact with.
    private final ArrayList<Usable> usableList;
    //The player's position and directionAngle when it is spawned.
    private SpawnPoint spawnPoint;
    //The angle that the player is facing.
    private final MutableDouble viewAngle;
    //The direction the player is moving relative to the camera.
    private double controlAngle;
    //The default height of the player.
    private final double height = 10;

    //Images for the tank in game and on the minimap.
    private final static BufferedImage[] sprites;
    private final static BufferedImage playerIcon;
    //The different sounds for the footstep.
    private final static String[] footstepSounds;

    //Determines if the playerController is dead, alive, or re-spawning.
    private boolean alive;

    //The size of the hit circle around the tank.
    private final static int hitCircleRadius = 8;
    //How much to scale the images when drawn to the screen.
    private final static double scale = 0.04;
    //How high projectiles are fired.
    private final double weaponHeight;
    //Stats of the tank.
    private final static int maxHealth = 100;
    private int health = maxHealth;

    //The weapons that the player has.
    private final ArrayList<Weapon> weapons;
    //The items that the player has.
    private final ArrayList<Item> items;

    //Load the images for the tank.
    static {
        //Load a blank buffered image as a placeholder.
        sprites = new BufferedImage[] { new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB) };
        //Load the default icon.
        playerIcon = Image.load("resources/HUD/Player Icon.png");

        //Populate the array of footstep sounds.
        footstepSounds = new String[] { "Footstep 1", "Footstep 2", "Footstep 3", "Footstep 4", "Footstep 5" };
    }

    public Player(SpawnPoint spawnPoint, ArrayList<Usable> usableList) {
        super(hitCircleRadius, new Point2D.Double(spawnPoint.getPosition().x, spawnPoint.getPosition().y), spawnPoint.getAngle(), 0);
        this.usableList = usableList;
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

        //Initialize the item and weapon array lists.
        weapons = new ArrayList<>();
        items = new ArrayList<>();
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

            //Add the distance moved to the distance since the last step.
            distanceSinceLastStep += speed * deltaTime / 1000;

            if(distanceSinceLastStep >= strideLength) {
                //Play a random footstep sound.
                SoundManager.playSound(footstepSounds[FastMath.random(0, footstepSounds.length - 1)]);
                distanceSinceLastStep = 0;
            }
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
        //If the player hits a wall, fix its position.
        else if(object instanceof Wall) {
            //If the player goes up the stairs, call its action method.
            if(object instanceof Stairs)
                ((Stairs)object).action();

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

    public void attack() {
        //If there is a weapon, attack with it.
        if(!weapons.isEmpty())
            weapons.get(0).attack();
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

    public void use() {
        //Only check for usable object if there are any.
        if(!usableList.isEmpty()) {
            //The object that the player will interact with.
            Usable object = null;
            //The distance to that object.
            double objectDistance = 0;

            //How far in the x and y directions the player can reach.
            double reachX = FastMath.cos(viewAngle.getValue()) * useReach;
            double reachY = FastMath.sin(viewAngle.getValue()) * useReach;

            //Create a line extending from the player towards where the player is looking with a length of useReach.
            Line2D.Double interactLine = new Line2D.Double(getPosition().x, getPosition().y, getPosition().x - reachX, getPosition().y - reachY);

            //Check if the interact line collides with any entities.
            for(Usable usable : usableList) {
                if (usable instanceof Entity) {
                    Entity entity = (Entity) usable;

                    //Rotate the entity's position to in front of the player
                    Point2D.Double rotatedPoint = new Point2D.Double(entity.position.x, entity.position.y);
                    FastMath.rotate(rotatedPoint, getPosition(), -viewAngle.getValue());
                    //Translate the position so that it is along the y-axis.
                    FastMath.subtract(rotatedPoint, position);

                    if (FastMath.isPointInCircle(new Point2D.Double(interactLine.x2, interactLine.y2), entity.position, entity.getHitCircleRadius()) ||
                            (rotatedPoint.x >= -entity.getHitCircleRadius() && rotatedPoint.x <= entity.getHitCircleRadius())) {
                        //If the entity is within reach and it is the first object checked or it is closer than the saved object, save the entity.
                        if (rotatedPoint.y >= 0 &&  rotatedPoint.y <= useReach && (object == null || rotatedPoint.y < objectDistance)) {
                            object = usable;
                            objectDistance = rotatedPoint.y;
                        }
                    }
                }
                else {
                    Wall wall = (Wall) usable;

                    //Get the line from the wall, rotate it to in front of the player, and translate it so it is in front of the origin.
                    Line2D.Double line = wall.getLine();
                    FastMath.rotate(line, position, -viewAngle.getValue());
                    FastMath.translate(line, -position.x, -position.y);

                    //Get the distance from the wall to the player.
                    double dist = FastMath.getYIntercept(line);

                    //If the wall is within reach and it is the first object checked or it is closer than the saved object, save the wall.
                    if (dist >= 0 &&  dist <= useReach && (object == null || dist < objectDistance)) {
                        object = usable;
                        objectDistance = dist;
                    }
                }
            }

            //Call the 'use' method on the nearest usable object in reach if there were any.
            if(object != null)
                object.use(this);
            //Otherwise if the interact line didn't collide with any objects, play an invalid sound.
            else
                SoundManager.playSound("Invalid");
        }
    }

    public void resetSpawn() {
        position.setLocation(spawnPoint.getPosition());
        viewAngle.setValue(spawnPoint.getAngle());
        health = maxHealth;
        alive = true;
    }

    public void reset() {
        resetSpawn();
        weapons.clear();
        items.clear();
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
    public void setSpawnPoint(SpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
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
    public double getWeaponHeight() {
        return weaponHeight;
    }
    public boolean isAlive() {
        return alive;
    }
    public void addWeapon(Weapon newWeapon) {
        weapons.add(newWeapon);
    }
    public void addItem(Item newItem) {
        items.add(newItem);
    }

    public Key getKey() {
        Key key = null;

        for(Item item : items) {
            if (item instanceof Key) {
                key = (Key) item;
                break;
            }
        }

        if(key != null)
            items.remove(key);

        return key;
    }
}