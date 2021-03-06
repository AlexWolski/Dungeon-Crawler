package DungeonCrawler.InputManager.KeyboardControls;

import DungeonCrawler.PlayerController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Back extends AbstractAction {
    private final PlayerController playerController;
    private final boolean keyPressed;

    public Back(PlayerController playerController, boolean keyPressed) {
        this.playerController = playerController;
        this.keyPressed = keyPressed;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerController.back(keyPressed);
        playerController.updateDirection();
    }
}
