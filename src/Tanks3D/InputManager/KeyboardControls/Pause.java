package Tanks3D.InputManager.KeyboardControls;

import Tanks3D.GameManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Pause extends AbstractAction {
    private boolean paused = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!paused) {
            paused = true;
            GameManager.pause();
        }
        else {
            paused = false;
            GameManager.unPause();
        }
    }
}
