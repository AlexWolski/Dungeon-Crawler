package Tanks3D.GameObject.Entity;

import Tanks3D.GameObject.Entity.Projectile.Projectile;
import Tanks3D.GameObject.GameObject;
import Tanks3D.GameObject.Update;
import Tanks3D.GameObject.Wall.Wall;
import Tanks3D.Utilities.FastMath;
import Tanks3D.GameData;
import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Entity extends GameObject implements Update {
    //The radius of the hit circle of the entity.
    private final int hitCircleRadius;
    //An array containing the sprites for the entity at different angles.
    private BufferedImage[] sprites;
    //An array containing the pixel data for each sprite.
    private byte[][] spritePixelData;
    //The icon used to display the entity on the minimap.
    private BufferedImage icon;
    //The in-game size of the entity. The sprites are stretched to fit this.
    private Dimension entitySize;

    //The color each image will be tinted.
    public Color entityColor;
    //The entity's data in 3d space.
    protected double zPos;
    public Point2D.Double position;
    public double directionAngle;
    public double speed;

    public Entity(int hitCircleRadius, Point2D.Double position, double directionAngle, double speed) {
        this.hitCircleRadius = hitCircleRadius;
        this.position = position;
        this.directionAngle = directionAngle;
        this.speed = speed;
    }

    //All entity classes have a 'collide' method that handles the event that it collides with a wall or another entity.
    //Pass an iterator to the entity and the object it collided with in case either needs to be removed from the entity list.
    public abstract void collide(Object object);

    public void update(GameData gamedata, double deltaTime) {
        if(this.visible) {
            if(speed != 0) {
                //Move the entity based on its directionAngle and speed.
                double distMoved = speed * deltaTime / 1000;
                //Equivalent of cos(directionAngle-90)
                position.x += distMoved * FastMath.sin(directionAngle);
                //Equivalent of sin(directionAngle-90)
                position.y += distMoved * FastMath.cos(directionAngle);
            }

            //Check if the entity collides with any walls or entities.
            //Pass an iterator to this object in case it needs to remove itself from the list.
            checkCollisionWall(gamedata.wallList);
            checkCollisionEntity(gamedata.entityList);
        }
    }

    public void isCollide(GameObject object) {
        if(object instanceof Wall) {

        } else if (object instanceof Entity) {

        }
    }

    //Check if this entity collides with any walls. If it does, pass the wall to the 'collide' method.
    private void checkCollisionWall(ArrayList<Wall> wallList) {
        //The line of the wall rotated so that the ray is along the y axis.
        Line2D.Double rotatedLine;

        //Iterator for checking all of the walls.
        for(Wall wall : wallList) {
            //Check for collisions with the wall if it is collidable.
            if(wall.getVisible() && ((this instanceof Player && wall.isCharacterCollidable()) || (this instanceof Projectile && wall.isProjectileCollidable()))) {
                //Check if the entity hits the sides of the wall. If it does, call the 'collide' method.
                if (FastMath.isPointInCircle(wall.getPoint1(), position, this.getHitCircleRadius()))
                    this.collide(wall.getPoint1());
                else if(FastMath.isPointInCircle(wall.getPoint2(), position, this.getHitCircleRadius()))
                    this.collide(wall.getPoint2());

                //Copy the line of the wall.
                rotatedLine = wall.getLine();
                //Rotate the line around the entity so that it is vertical.
                FastMath.rotate(rotatedLine, this.position, -wall.getAngle());

                //If the distance to the line is less than the hit circle radius and the line is next to the entity, call the 'collide' method.
                if (Math.abs(rotatedLine.x1 - this.position.x) < this.getHitCircleRadius()
                        && ((rotatedLine.y1 >= this.position.y - this.getHitCircleRadius()
                        && rotatedLine.y2 <= this.position.y) || (rotatedLine.y1 <= this.position.y
                        && rotatedLine.y2 >= this.position.y))) {
                    //Pass the iterator in case the entity needs to delete the wall.
                    this.collide(wall);
                }
            }
        }
    }

    //Check if this entity collides with any other entities.  If it does, pass the other entity to the 'collide' method.
    private void checkCollisionEntity(ArrayList<Entity> entityList) {
        //The distance between the two entities squared.
        double actualDistSquared;
        //The distance where the two entities touch squared.
        double minDistSquared;

        //Iterate through the array of entities and check if the entity collides with it.
        for(Entity entity : entityList) {
            //Prevent collisions between an entity and itself.
            if(this != entity) {
                //Calculate the two distances.
                actualDistSquared = Math.pow(entity.position.x - this.position.x, 2) + Math.pow(entity.position.y - this.position.y, 2);
                minDistSquared = Math.pow(entity.getHitCircleRadius() + this.getHitCircleRadius(), 2);

                //If the entities collide, call the collide method of this entity.
                if (actualDistSquared < minDistSquared)
                    //Pass the iterator in case this entity needs to delete the other.
                    this.collide(entity);
            }
        }
    }

    public int getHitCircleRadius() {
        return hitCircleRadius;
    }
    public double getHeight() {
        return entitySize.height;
    }
    public double getWidth() {
        return entitySize.width;
    }

    //Set the sprites and size of the entity. The sprites start with a front shot and rotate clockwise.
    protected void setSprites(BufferedImage[] images, int width, int height) {
        this.sprites = images;
        this.entitySize = new Dimension(width, height);

        //Initialize the array of image pixel data.
        spritePixelData = new byte[sprites.length][];

        //Get the pixel data of each sprite.
        for(int i = 0; i < sprites.length; i++)
            spritePixelData[i] = Image.getABGRColorData(sprites[i]);
    }

    //Set the icon for the minimap.
    protected void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    //Given the directionAngle of the camera, return the appropriate image.
    public BufferedImage getSprite(double viewerAngle) {
        //Get the difference in directionAngle between the entity and the viewer.
        viewerAngle = FastMath.formatAngle(this.directionAngle - viewerAngle - 540.0/ sprites.length);
        //Map the directionAngle to one of the sprites and return it.
        return sprites[(int)(viewerAngle * sprites.length/360)];
    }

    //Given the directionAngle of the camera, return the pixel data corresponding to the appropriate image.
    public byte[] getSpritePixelData(double viewerAngle) {
        //Get the difference in directionAngle between the player and the viewer.
        viewerAngle = FastMath.formatAngle(this.directionAngle - viewerAngle - 540.0/ sprites.length);
        //Map the directionAngle to one of the sprites and return it.
        return spritePixelData[(int)(viewerAngle * sprites.length/360)];
    }

    //Get the icon for the minimap.
    public BufferedImage getIcon() {
        return icon;
    }
    //Get the in game height of the entity.
    public double getzPos() {
        return zPos;
    }
}
