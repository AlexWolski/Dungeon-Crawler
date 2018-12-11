package Tanks3D;

import Tanks3D.DisplayComponents.Camera.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.Object.Entity.Player;
import Tanks3D.Object.SpawnPoint;
import Tanks3D.Utilities.Wrappers.MutableDouble;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Manage the player and the screen. This extends 'Runnable' so that the draw function can be threaded.
public class PlayerController {
    private final Player myPlayer;
    //A camera for displaying the game world.
    private final Camera camera;
    //The heads up display for the playerController.
    private final HUD hud;
    //Remember what keys are being pressed. When the player is created, it isn't moving or firing.
    private boolean moveForward, moveBackward, moveLeft, moveRight, lookLeft, lookRight, fire = false;
    //Determine if the game is drawing to the screen to prevent a concurrent modification exception.
    private boolean drawing = false;
    //Determine whether to display the 'You Win' or 'You Lose' images.
    private boolean win, lose = false;

    public PlayerController(GameData gameData, BufferedImage canvas, SpawnPoint spawnPoint, Color playerColor) {
        //Create a new player given the spawn-point and add it to the entity list.
        myPlayer = new Player(spawnPoint.getPosition(), spawnPoint.getAngle(), playerColor);
        //Add the new player to the list of all entities.
        gameData.entityList.add(myPlayer);

        //Create a new camera given the spawn-point position and directionAngle.
        camera = new Camera(gameData, canvas, getPosition(), myPlayer.getzPos(), getAngle());

        //Create a new HUD object with the player's color.
        hud = new HUD(canvas, getColor());
    }

    //Draw the playerController's screen.
    public void draw() {
        //If the player is alive, check if it has any lives left.
        if(!myPlayer.isAlive()) {
            //If it has lives left, respawn the player and reset the controls.
            if(myPlayer.getLives() > 0) {
                myPlayer.respawn();
                moveForward = moveBackward = moveLeft = moveRight = false;
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

        //If the fire key was pressed while the screen was being drawn, fire the player after drawing is finished.
        if(fire) {
            myPlayer.fire();
            fire = false;
        }
    }

    protected void reset() {
        win = lose = false;
        myPlayer.resetPlayer();
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
    public Player getPlayer() {
        return myPlayer;
    }

    //Update the direction the player is moving based on what keys are pressed.
    public void updateDirection() {
        //The angle of direction perpendicular to the camera and parallel to the camera.
        double xAngle = -1, yAngle = -1;

        //Get the direction perpendicular to the camera.
        if(moveForward && !moveBackward)
            yAngle = 0;
        else if(moveBackward && !moveForward)
            yAngle = 180;

        //Get the direction parallel to the camera.
        if(moveRight && !moveLeft)
            xAngle = 90;
        else if(moveLeft && !moveRight)
            xAngle = -90;

        //If the player isn't moving, exit.
        if(xAngle == -1 && yAngle == -1) {
            myPlayer.speed = 0;
            return;
        }

        //If the player is only moving in the x or y direction, add it to the player's camera and set the entity direction.
        else if(xAngle == -1)
            myPlayer.setControlAngle(yAngle);
        else if(yAngle == -1)
            myPlayer.setControlAngle(xAngle);
        //Otherwise, get the average between the x and y directions, and add it to the entity direction.
        else {
            if(yAngle - xAngle > 180)
                yAngle -= 360;

            myPlayer.setControlAngle((xAngle + yAngle)/2);
        }

        //Set the speed of the player.
        myPlayer.speed = myPlayer.getMaxSpeed();
    }

    //If the forward key has been pressed or released, update the player's speed.
    public void forward(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !moveForward) {
                moveForward = true;
            } else if (!keyPressed && moveForward) {
                moveForward = false;
            }
        }
    }

    //If the back key has been pressed or released, update the player's speed.
    public void back(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !moveBackward) {
                moveBackward = true;
            } else if (!keyPressed && moveBackward) {
                moveBackward = false;
            }
        }
    }

    //If the left key has been pressed or released, update the player's rotation.
    public void left(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !moveLeft) {
                moveLeft = true;
            } else if (!keyPressed && moveLeft) {
                moveLeft = false;
            }
        }
    }

    //If the right key has been pressed or released, update the player's rotation.
    public void right(boolean keyPressed) {
        //Only move the playerController if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !moveRight) {
                moveRight = true;
            } else if (!keyPressed && moveRight) {
                moveRight = false;
            }
        }
    }

    //If the look left key has been pressed or released, update the player's rotation.
    public void lookLeft(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !lookLeft) {
                myPlayer.rotationSpeed -= myPlayer.getMaxRotationSpeed();
                lookLeft = true;
            } else if (!keyPressed && lookLeft) {
                myPlayer.rotationSpeed += myPlayer.getMaxRotationSpeed();
                lookLeft = false;
            }
        }
    }

    //If the look right key has been pressed or released, update the player's rotation.
    public void lookRight(boolean keyPressed) {
        //Only move the player if they are alive.
        if(myPlayer.isAlive()) {
            if (keyPressed && !lookRight) {
                myPlayer.rotationSpeed += myPlayer.getMaxRotationSpeed();
                lookRight = true;
            } else if (!keyPressed && lookRight) {
                myPlayer.rotationSpeed -= myPlayer.getMaxRotationSpeed();
                lookRight = false;
            }
        }
    }

    //If the fire key was pressed, tell the player to fire.
    public void fire() {
        //If the playerController is alive, fire.
        if(myPlayer.isAlive())
            //If the screen isn't currently being drawn, fire the player now.
            if(!drawing)
                myPlayer.fire();
            //If the screen is being drawn, indicate to fire the player after it's done.
            else
                fire = true;
    }
}