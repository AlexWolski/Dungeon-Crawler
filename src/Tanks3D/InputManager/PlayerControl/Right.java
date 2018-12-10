package Tanks3D.InputManager.PlayerControl;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Right extends AbstractAction {
    private final PlayerController playerController;
    private final boolean keyPressed;

    public Right(PlayerController playerController, boolean keyPressed) {
        this.playerController = playerController;
        this.keyPressed = keyPressed;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.right(keyPressed);
        playerController.updateDirection();
    }
}
