package Tanks3D.InputManager.PlayerControl;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Forward extends AbstractAction {
    private final PlayerController playerController;
    private final boolean keyPressed;

    public Forward(PlayerController playerController, boolean keyPressed) {
        this.playerController = playerController;
        this.keyPressed = keyPressed;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.forward(keyPressed);
        playerController.updateDirection();
    }
}
