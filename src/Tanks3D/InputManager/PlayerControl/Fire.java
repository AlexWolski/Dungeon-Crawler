package Tanks3D.InputManager.PlayerControl;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Fire extends AbstractAction {
    private final PlayerController playerController;

    public Fire(PlayerController playerController) {
        this.playerController = playerController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.fire();
    }
}
