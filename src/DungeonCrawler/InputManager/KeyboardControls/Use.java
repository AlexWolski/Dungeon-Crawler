package DungeonCrawler.InputManager.KeyboardControls;

import DungeonCrawler.PlayerController;

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
