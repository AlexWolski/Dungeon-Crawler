package Tanks3D;

import Tanks3D.Object.Entity.Player;

//Manage the player and the screen. This extends 'Runnable' so that the draw function can be threaded.
public class PlayerController {
    private final Player myPlayer;
    //Remember what keys are being pressed. When the player is created, it isn't moving or firing.
    private boolean moveForward, moveBackward, moveLeft, moveRight, lookLeft, lookRight, firePressed = false;

    public PlayerController(Player player) {
        myPlayer = player;
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

    //If the attack key was pressed, tell the player to attack.
    public void fire(boolean keyPressed) {
        //If the playerController is alive, attack.
        if(myPlayer.isAlive()) {
            if (keyPressed && !firePressed) {
                //If the screen isn't currently being drawn, attack the player now.
                myPlayer.attack();
                firePressed = true;
            }
            else if(!keyPressed && firePressed)
                firePressed = false;
        }
    }

    //Rotate the player by the given amount.
    public void rotatePlayer(double angle) {
        myPlayer.setViewAngle(myPlayer.getViewAngle().getValue() + angle);
    }
}