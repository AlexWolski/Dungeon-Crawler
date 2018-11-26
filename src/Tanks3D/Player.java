package Tanks3D;

import Tanks3D.DisplayComponents.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.Object.Entity.Tank;
import Tanks3D.Object.SpawnPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Manage the player's tank and screen. This extends 'Runnable' so that the draw function can be threaded.
public class Player implements Runnable {
    private final Tank myTank;
    //A camera for displaying the game world.
    private final Camera camera;
    //The heads up display for the player.
    private final HUD hud;
    //Remember what keys are being pressed. When the tank is created, it isn't moving or firing.
    private boolean forwardPressed, backPressed, leftPressed, rightPressed, firePressed = false;
    //An in that determines if the controls are inverted or not. Either -1 or 1.
    private int invertControls = 1;
    //Determine if the game is drawing to the screen to prevent a concurrent modification exception.
    private boolean drawing = false;
    //Determine whether to display the 'You Win' or 'You Lose' images.
    private boolean win, lose = false;

    public Player(GameData gameData, BufferedImage canvas, SpawnPoint spawnPoint, Color tankColor, String iconSide) {
        //Create a new tank given the spawn-point and add it to the entity list.
        myTank = new Tank(spawnPoint.getPosition(), spawnPoint.getAngle(), tankColor);
        //Add the new tank to the list of all entities.
        gameData.entityList.add(myTank);

        //Create a new camera given the spawn-point position and angle.
        camera = new Camera(gameData, canvas);

        //Create a new HUD object with the tank's color. 'iconSide' determines which side of the screen the HUD icons are on.
        hud = new HUD(canvas, getColor(), iconSide);
    }

    //Draw the player's screen.
    private void draw() {
        //If the tank is alive, check if it has any lives left.
        if(!myTank.isAlive()) {
            //If it has lives left, respawn the tank and reset the controls.
            if(myTank.getLives() > 0) {
                myTank.respawn();
                forwardPressed = backPressed = leftPressed = rightPressed = false;
                invertControls = 1;
            }
            //Otherwise, end the game.
            else
                GameManager.loseGame(this);

        }

        //Indicate that the screen is being drawn.
        drawing = true;

        //Draw the walls and entities.
        camera.draw(myTank.getPosition(), myTank.getAngle());
        //Draw the HUD.
        hud.draw(myTank.getMaxHealth(), myTank.getHealth(), myTank.getLives(), win, lose, myTank.isAlive());

        //Indicate that the screen is finished drawing.
        drawing = false;

        //If the fire key was pressed while the screen was being drawn, fire the tank after drawing is finished.
        if(firePressed) {
            myTank.fire();
            firePressed = false;
        }
    }

    protected void lose() {
        lose = true;
    }
    protected void win() {
        win = true;
    }

    protected void reset() {
        win = lose = false;
        myTank.resetTank();
        myTank.resetLives();
    }

    //Draw the player's screen in a thread.
    public void run() {
        draw();
    }

    public Color getColor() {
        return myTank.getColor();
    }
    public Point2D.Double getPosition() {
        return myTank.getPosition();
    }
    public double getAngle() {
        return myTank.getAngle();
    }
    public Tank getTank() {
        return myTank;
    }

    //If the forward key has been pressed or released, update the tank's speed.
    public void forward(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myTank.isAlive()) {
            if (keyPressed && !forwardPressed) {
                myTank.speed += myTank.getMaxSpeed();
                forwardPressed = true;
            } else if (!keyPressed && forwardPressed) {
                myTank.speed -= myTank.getMaxSpeed();
                forwardPressed = false;
            }
        }
    }

    //If the back key has been pressed or released, update the tank's speed.
    public void back(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myTank.isAlive()) {
            if (keyPressed && !backPressed) {
                myTank.speed -= myTank.getMaxSpeed();
                backPressed = true;

                //Invert the left and right controls.
                myTank.rotationSpeed *= -1;
                invertControls *= -1;
            } else if (!keyPressed && backPressed) {
                myTank.speed += myTank.getMaxSpeed();
                backPressed = false;

                //Revert the left and right controls.
                myTank.rotationSpeed *= -1;
                invertControls *= -1;
            }
        }
    }

    //If the left key has been pressed or released, update the tank's rotation.
    public void left(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myTank.isAlive()) {
            if (keyPressed && !leftPressed) {
                myTank.rotationSpeed -= myTank.getMaxRotationSpeed() * invertControls;
                leftPressed = true;
            } else if (!keyPressed && leftPressed) {
                myTank.rotationSpeed += myTank.getMaxRotationSpeed() * invertControls;
                leftPressed = false;
            }
        }
    }

    //If the right key has been pressed or released, update the tank's rotation.
    public void right(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myTank.isAlive()) {
            if (keyPressed && !rightPressed) {
                myTank.rotationSpeed += myTank.getMaxRotationSpeed() * invertControls;
                rightPressed = true;
            } else if (!keyPressed && rightPressed) {
                myTank.rotationSpeed -= myTank.getMaxRotationSpeed() * invertControls;
                rightPressed = false;
            }
        }
    }

    //If the fire key was pressed, tell the tank to fire.
    public void fire() {
        //If the player is alive, fire.
        if(myTank.isAlive())
            //If the screen isn't currently being drawn, fire the tank now.
            if(!drawing)
                myTank.fire();
            //If the screen is being drawn, indicate to fire the tank after it's done.
            else
                firePressed = true;
    }
}