package Tanks3D;

import Tanks3D.DisplayComponents.Camera.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.Object.Entity.Player;
import Tanks3D.Object.SpawnPoint;
import Tanks3D.Utilities.Wrappers.MutableDouble;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Manage the playerController's tank and screen. This extends 'Runnable' so that the draw function can be threaded.
public class PlayerController {
    private final Player myPlayer;
    //A camera for displaying the game world.
    private final Camera camera;
    //The heads up display for the playerController.
    private final HUD hud;
    //Remember what keys are being pressed. When the tank is created, it isn't moving or firing.
    private boolean forwardPressed, backPressed, leftPressed, rightPressed, firePressed = false;
    //An in that determines if the controls are inverted or not. Either -1 or 1.
    private int invertControls = 1;
    //Determine if the game is drawing to the screen to prevent a concurrent modification exception.
    private boolean drawing = false;
    //Determine whether to display the 'You Win' or 'You Lose' images.
    private boolean win, lose = false;

    public PlayerController(GameData gameData, BufferedImage canvas, SpawnPoint spawnPoint, Color tankColor) {
        //Create a new tank given the spawn-point and add it to the entity list.
        myPlayer = new Player(spawnPoint.getPosition(), spawnPoint.getAngle(), tankColor);
        //Add the new tank to the list of all entities.
        gameData.entityList.add(myPlayer);

        //Create a new camera given the spawn-point position and angle.
        camera = new Camera(gameData, canvas, getPosition(), getAngle());

        //Create a new HUD object with the tank's color.
        hud = new HUD(canvas, getColor());
    }

    //Draw the playerController's screen.
    public void draw() {
        //If the tank is alive, check if it has any lives left.
        if(!myPlayer.isAlive()) {
            //If it has lives left, respawn the tank and reset the controls.
            if(myPlayer.getLives() > 0) {
                myPlayer.respawn();
                forwardPressed = backPressed = leftPressed = rightPressed = false;
                invertControls = 1;
            }
            //Otherwise, end the game.
            else {
                GameManager.endGame();
                lose = true;
            }

        }

        //Indicate that the screen is being drawn.
        drawing = true;

        //Draw the walls and entities.
        camera.draw();
        //Draw the HUD.
        hud.draw(myPlayer.getMaxHealth(), myPlayer.getHealth(), myPlayer.getLives(), win, lose, myPlayer.isAlive());

        //Indicate that the screen is finished drawing.
        drawing = false;

        //If the fire key was pressed while the screen was being drawn, fire the tank after drawing is finished.
        if(firePressed) {
            myPlayer.fire();
            firePressed = false;
        }
    }

    protected void reset() {
        win = lose = false;
        myPlayer.resetTank();
        myPlayer.resetLives();
    }

    public Color getColor() {
        return myPlayer.getColor();
    }
    public Point2D.Double getPosition() {
        return myPlayer.getPosition();
    }
    public MutableDouble getAngle() {
        return myPlayer.getAngle();
    }
    public Player getTank() {
        return myPlayer;
    }

    //If the forward key has been pressed or released, update the tank's speed.
    public void forward(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !forwardPressed) {
                myPlayer.speed += myPlayer.getMaxSpeed();
                forwardPressed = true;
            } else if (!keyPressed && forwardPressed) {
                myPlayer.speed -= myPlayer.getMaxSpeed();
                forwardPressed = false;
            }
        }
    }

    //If the back key has been pressed or released, update the tank's speed.
    public void back(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !backPressed) {
                myPlayer.speed -= myPlayer.getMaxSpeed();
                backPressed = true;

                //Invert the left and right controls.
                myPlayer.rotationSpeed *= -1;
                invertControls *= -1;
            } else if (!keyPressed && backPressed) {
                myPlayer.speed += myPlayer.getMaxSpeed();
                backPressed = false;

                //Revert the left and right controls.
                myPlayer.rotationSpeed *= -1;
                invertControls *= -1;
            }
        }
    }

    //If the left key has been pressed or released, update the tank's rotation.
    public void left(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !leftPressed) {
                myPlayer.rotationSpeed -= myPlayer.getMaxRotationSpeed() * invertControls;
                leftPressed = true;
            } else if (!keyPressed && leftPressed) {
                myPlayer.rotationSpeed += myPlayer.getMaxRotationSpeed() * invertControls;
                leftPressed = false;
            }
        }
    }

    //If the right key has been pressed or released, update the tank's rotation.
    public void right(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !rightPressed) {
                myPlayer.rotationSpeed += myPlayer.getMaxRotationSpeed() * invertControls;
                rightPressed = true;
            } else if (!keyPressed && rightPressed) {
                myPlayer.rotationSpeed -= myPlayer.getMaxRotationSpeed() * invertControls;
                rightPressed = false;
            }
        }
    }

    //If the fire key was pressed, tell the tank to fire.
    public void fire() {
        //If the playerController is alive, fire.
        if(myPlayer.isAlive())
            //If the screen isn't currently being drawn, fire the tank now.
            if(!drawing)
                myPlayer.fire();
            //If the screen is being drawn, indicate to fire the tank after it's done.
            else
                firePressed = true;
    }
}