package Tanks3D.GameObject.Wall.Door;

import Tanks3D.GameData;
import Tanks3D.GameObject.Entity.Player;
import Tanks3D.GameObject.Update;
import Tanks3D.GameObject.Usable;
import Tanks3D.GameObject.Wall.Wall;
import Tanks3D.Utilities.FastMath;

import java.awt.*;
import java.awt.geom.Point2D;

public class Door extends Wall implements Usable, Update {
    //How fast the door opens, in degrees per second.
    protected final static double swingSpeed = 100.0;

    //The angle of the door when it is closed.
    protected final double defaultAngle;
    //The angle of the door when its fully opened in one direction.
    protected final double minAngle;
    //The angle of the door when its fully opened in the other direction.
    protected final double maxAngle;

    //The door will keep moving until it reaches this angle.
    protected double targetAngle;
    //Determines if the door is moving or not.
    protected boolean isMoving;
    //Determines the direction the door is moving.
    protected boolean clockwise;

    public Door(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String textureName, double swingAngle, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, textureName, textureColor);
        //Calculate the bounds of the movement for the door.
        defaultAngle = angle;
        minAngle = defaultAngle - swingAngle;
        maxAngle = defaultAngle + swingAngle;

        isMoving = false;
    }

    public void use(Player player) {
        //Only move the door if it isn't already moving.
        if(!isMoving) {
            //If the door is open, close it.
            if (angle == minAngle || angle == maxAngle)
                targetAngle = defaultAngle;
            //Else,
            else
                targetAngle = minAngle;

            //Determine if the wall will rotate clockwise or counter clockwise.
            if(targetAngle > angle)
                clockwise = true;
            else
                clockwise = false;

            isMoving = true;
        }
    }

    public void update(GameData gameData, double deltaTime) {
        if(isMoving) {
            double angleMoved = swingSpeed * deltaTime / 1000;

            if(!clockwise)
                angleMoved *= -1;

            if(angle < targetAngle && angle + angleMoved > targetAngle) {
                isMoving = false;
                angleMoved = targetAngle - angle;
                angle = targetAngle;
            }
            else if(angle > targetAngle && angle + angleMoved < targetAngle) {
                isMoving = false;
                angleMoved = targetAngle - angle;
                angle = targetAngle;
            }
            else
                angle += angleMoved;

            Point2D.Double rotatedPoint = new Point2D.Double(line.x2, line.y2);
            FastMath.rotate(rotatedPoint, getPoint1(), angleMoved);
            line.setLine(line.x1, line.y1, rotatedPoint.x, rotatedPoint.y);
        }
    }
}