package Tanks3D.InputManager.KeyboardControls;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LookLeft extends AbstractAction {
    private final PlayerController playerController;
    private final boolean keyPressed;

    public LookLeft(PlayerController playerController, boolean keyPressed) {
        this.playerController = playerController;
        this.keyPressed = keyPressed;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.lookLeft(keyPressed);
        playerController.updateDirection();
    }
}