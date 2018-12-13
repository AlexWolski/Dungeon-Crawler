package Tanks3D.InputManager.KeyboardControls;

import Tanks3D.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Use extends AbstractAction {
    private final PlayerController playerController;

    public Use (PlayerController playerController) {
        this.playerController = playerController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.use();
    }
}
