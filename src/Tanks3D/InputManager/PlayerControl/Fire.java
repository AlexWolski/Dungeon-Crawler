package Tanks3D.InputManager.PlayerControl;

import Tanks3D.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Fire extends AbstractAction {
    private final Player player;

    public Fire(Player player) {
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.fire();
    }
}
