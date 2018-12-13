package Tanks3D.GameObject.Wall;

import Tanks3D.GameManager;
import Tanks3D.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;

public class Stairs extends Wall {
    private boolean triggered = false;

    public Stairs(Point2D.Double Point1, Point2D.Double Point2, String textureName, Color textureColor) {
        super(Point1, Point2, false, true, true, textureName, textureColor);
    }

    public void action() {
        if(!triggered) {
            //Play the stairs sound effect.
            SoundManager.playSound("Stairs");
            //Move on to the next level.
            GameManager.nextLevel();

            //Only activate the stairs once.
            triggered = true;
        }
    }
}
