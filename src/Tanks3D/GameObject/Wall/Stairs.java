package Tanks3D.GameObject.Wall;

import Tanks3D.GameManager;
import Tanks3D.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;

public class Stairs extends Wall {
    public Stairs(Point2D.Double Point1, Point2D.Double Point2, String textureName, Color textureColor) {
        super(Point1, Point2, false, true, true, textureName, textureColor);
    }

    public void action() {
        //Play the stairs sound effect.
        SoundManager.playSound("Stairs");
        //Prevent the player from triggering the action more than once.
        characterCollidable = false;
        //
        GameManager.nextLevel();
    }
}
