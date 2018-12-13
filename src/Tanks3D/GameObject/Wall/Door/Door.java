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
    protected final static double swingSpeed = 2.0;
    //How far the door can move from its original position.
    protected final static double swingAngle = 85.0;

    //The angle of the door when it is closed.
    protected final double closedAngle;
    //The angle of the door when its fully opened in one direction.
    protected final double minAngle;
    //The angle of the door when its fully opened in the other direction.
    protected final double maxAngle;

    //The door will keep moving until it reaches this angle.
    protected double targetAngle;
    //Determines if the door is moving or not.
    protected boolean isMoving;

    public Door(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String textureName, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, textureName, textureColor);
        //Calculate the bounds of the movement for the door.
        closedAngle = angle;
        minAngle = closedAngle - swingAngle;
        maxAngle = closedAngle + swingAngle;

        isMoving = false;
    }

    public void use(Player player) {
        targetAngle = minAngle;
        isMoving = true;
    }

    public void update(GameData gameData, double deltaTime) {
        if(angle != targetAngle) {
            double angleMoved = swingAngle * deltaTime / 1000;

            angle += angleMoved;

            Point2D.Double rotatedPoint = new Point2D.Double(line.x2, line.y2);
            FastMath.rotate(rotatedPoint, getPoint1(), angleMoved);
            line.setLine(line.x1, line.y1, rotatedPoint.x, rotatedPoint.y);
        }
    }
}