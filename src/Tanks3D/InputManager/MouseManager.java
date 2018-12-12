package Tanks3D.InputManager;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//Handles mouse inputs.
public class MouseManager extends MouseAdapter {
    //The frame that the game is running in.
    private static JFrame window;
    //The controller class for the player.
    private static PlayerController playerController;
    //The dimensions of the frame.
    private static Dimension dimension;
    //A robot object used to move the mouse.
    private static Robot robot;
    //A cursor with no mouse icon.
    private static Cursor hiddenCursor;
    //Determine if the game is paused or not.
    private static boolean paused;

    //The sensitivity of the mouse.
    private double sensitivity = 0.05;

    //Create a MouseManager object.
    public static void init(JFrame frame, PlayerController controller) {
        window = frame;
        playerController = controller;
        dimension = window.getSize();

        //The game is not paused by default.
        paused = false;

        //Create the robot object.
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Create the hidden cursor.
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(dummy, new Point(0, 0),"hidden cursor");
        window.setCursor(hiddenCursor);
        //Move the cursor to the center of the screen.
        robot.mouseMove(window.getLocation().x + dimension.width / 2, window.getLocation().y + dimension.height / 2);

        //Add the mouse manager to the JFrame.
        new MouseManager();
    }

    //Add this mouse manager to the window.
    private MouseManager() {
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
    }

    public static void pause() {
        window.setCursor(Cursor.getDefaultCursor());
        paused = true;
    }

    public static void unPause() {
        Point location = window.getLocation();
        window.setCursor(hiddenCursor);
        robot.mouseMove(location.x + dimension.width / 2, location.y + dimension.height / 2);
        paused = false;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        //Only fire if the left mouse button was pressed.
        if(SwingUtilities.isLeftMouseButton(event))
            playerController.fire(true);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        playerController.fire(false);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if(!paused) {
            Point location = window.getLocation();
            playerController.rotatePlayer((event.getX() - dimension.width / 2.0) * sensitivity);
            robot.mouseMove(location.x + dimension.width / 2, location.y + dimension.height / 2);
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if(!paused) {
            Point location = window.getLocation();
            playerController.rotatePlayer((event.getX() - dimension.width / 2.0) * sensitivity);
            robot.mouseMove(location.x + dimension.width / 2, location.y + dimension.height / 2);
        }
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
