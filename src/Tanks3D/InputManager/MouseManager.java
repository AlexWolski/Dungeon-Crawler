package Tanks3D.InputManager;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Handles mouse inputs.
public class MouseManager extends MouseAdapter {
    //The position of the frame on the screen.
    private final Point location;
    //The dimensions of the frame.
    private final Dimension dimension;
    //The controller class for the player.
    private final PlayerController playerController;
    //A robot object used to move the mouse.
    private Robot robot;

    //Create a MouseManager object.
    public static void init(JFrame window, PlayerController playerController) {
        new MouseManager(window, playerController);
    }

    //MouseManager is a singleton class.
    private MouseManager(JFrame window, PlayerController playerController) {
        this.playerController = playerController;
        location = window.getLocation();
        dimension = window.getSize();

        //Create the robot object.
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Add this mouse manager to the window.
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        playerController.fire(true);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        playerController.fire(false);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        playerController.rotatePlayer((event.getX() - dimension.width/2.0) * 0.05);
        robot.mouseMove(location.x + dimension.width/2, location.y + dimension.height/2);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        playerController.rotatePlayer((event.getX() - dimension.width/2.0) * 0.05);
        robot.mouseMove(location.x + dimension.width/2, location.y + dimension.height/2);
    }

    //Unused methods.
    @Override
    public void mouseClicked(MouseEvent event) {
    }
    @Override
    public void mouseEntered(MouseEvent event) {
    }
    @Override
    public void mouseExited(MouseEvent event) {
    }
}
