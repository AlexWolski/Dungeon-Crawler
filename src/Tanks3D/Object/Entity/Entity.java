package Tanks3D.Object.Entity;

import Tanks3D.Object.GameObject;
import Tanks3D.Object.Wall.UnbreakableWall;
import Tanks3D.Object.Wall.Wall;
import Tanks3D.Utilities.FastMath;
import Tanks3D.GameData;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Entity extends GameObject {
    //The radius of the hit circle of the entity.
    private final int hitCircleRadius;
    //An array containing the sprites for the entity at different angles.
    private BufferedImage sprites[];
    //The icon used to display the entity on the minimap.
    private BufferedImage icon;
    //The in-game size of the entity. The sprites are stretched to fit this.
    private Dimension entitySize;

    //The color each image will be tinted.
    public Color entityColor;
    //The entity's data in 3d space.
    protected int zPos;
    public Point2D.Double position;
    public double angle;
    public double speed;

    public Entity(int hitCircleRadius, Point2D.Double position, double angle, double speed) {
        this.hitCircleRadius = hitCircleRadius;
        this.position = position;
        this.angle = angle;
        this.speed = speed;
    }

    //All entity classes have a 'collide' method that handles the event that it collides with a wall or another entity.
    //Pass an iterator to the entity and the object it collided with in case either needs to be removed from the entity list.
    public abstract void collide(Object object, ListIterator thisObject, ListIterator collidedObject);

    public void update(GameData gamedata, double deltaTime, ListIterator<Entity> thisObject) {
        if(this.visible) {
            //Move the entity based on its angle and speed.
            double distMoved = speed * deltaTime / 1000;
            //Equivalent of cos(angle-90)
            position.x += distMoved * FastMath.sin(angle);
            //Equivalent of sin(angle-90)
            position.y += distMoved * FastMath.cos(angle);

            //Check if the entity collides with any walls or entities.
            //Pass an iterator to this object in case it needs to remove itself from the list.
            checkCollisionWall(gamedata.gameLevel.wallObjects, thisObject);
            checkCollisionEntity(gamedata.entityList, thisObject);
        }
    }

    //Check if this entity collides with any walls. If it does, pass the wall to the 'collide' method.
    private void checkCollisionWall(ArrayList<Wall> wallList, ListIterator<Entity> thisObject) {
        //The line of the wall rotated so that the ray is along the y axis.
        Line2D.Double rotatedLine;
        //Iterator for checking all of the walls.

        //A temporary wall object to reference the wall being checked.
        Wall wall;

        //Iterate through the array of walls and check if the entity collides with it.
        for(ListIterator<Wall> collidedObject = wallList.listIterator(); collidedObject.hasNext();) {
            //Store the wall being checked.
            wall = collidedObject.next();

            //Check for collisions with the wall if it is visible.
            if(wall.getVisible()) {
                //Check if the entity hits the sides of the wall. If it does, call the 'collide' method.
                if(wall instanceof UnbreakableWall)
                    if (FastMath.isPointInCircle(wall.getPoint1(), position, this.getHitCircleRadius()))
                        this.collide(wall.getPoint1(), thisObject, collidedObject);
                    else if(FastMath.isPointInCircle(wall.getPoint2(), position, this.getHitCircleRadius()))
                        this.collide(wall.getPoint2(), thisObject, collidedObject);

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
                    this.collide(wall, thisObject, collidedObject);
                }
            }
        }
    }

    //Check if this entity collides with any other entities.  If it does, pass the other entity to the 'collide' method.
    private void checkCollisionEntity(ArrayList<Entity> entityList, ListIterator<Entity> thisObject) {
        //The distance between the two entities squared.
        double actualDistSquared;
        //The distance where the two entities touch squared.
        double minDistSquared;

        //Iterator for checking all of the entities.
        ListIterator<Entity> collidedObject = entityList.listIterator();
        //A temporary entity object to reference the entity being checked.
        Entity entity;

        //Iterate through the array of entities and check if the entity collides with it.
        while(collidedObject.hasNext()) {
            //Store the wall being checked.
            entity = collidedObject.next();

            //Prevent collisions between an entity and itself.
            if(this != entity) {
                //Calculate the two distances.
                actualDistSquared = Math.pow(entity.position.x - this.position.x, 2) + Math.pow(entity.position.y - this.position.y, 2);
                minDistSquared = Math.pow(entity.getHitCircleRadius() + this.getHitCircleRadius(), 2);

                //If the entities collide, call the collide method of this entity.
                if (actualDistSquared < minDistSquared)
                    //Pass the iterator in case this entity needs to delete the other.
                    this.collide(entity, thisObject, collidedObject);
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
    protected void setSprites(BufferedImage images[], int width, int height) {
        this.sprites = images;
        this.entitySize = new Dimension(width, height);
    }

    //Set the icon for the minimap.
    protected void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    //Given the angle of the camera, return the appropriate image.
    public BufferedImage getSprite(double viewerAngle) {
        //Get the difference in angle between the tank and the viewer.
        viewerAngle = FastMath.formatAngle(this.angle - viewerAngle - 540.0/ sprites.length);
        //Map the angle to one of the sprites and return it.
        return sprites[(int)(viewerAngle * sprites.length/360)];
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
