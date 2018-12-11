package Tanks3D.InputManager;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Handles mouse inputs.
public class MouseManager extends MouseAdapter {
    //Create a MouseManager object.
    public static void init(JComponent window, PlayerController playerController) {
        MouseManager mouseManager = new MouseManager(window, playerController);
    }

    //MouseManager is a singleton class.
    private MouseManager(JComponent window, PlayerController playerController) {
        //Add this mouse manager to the window.
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        System.out.println("pressed");
    }

    @Override
    public void mouseExited(MouseEvent event) {
        System.out.println("exited");
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        System.out.println("moved");
    }

    //Unused methods.
    @Override
    public void mouseClicked(MouseEvent event) {
    }
    @Override
    public void mouseReleased(MouseEvent event) {
    }
    @Override
    public void mouseEntered(MouseEvent event) {
    }
    @Override
    public void mouseDragged(MouseEvent event) {
    }
}
