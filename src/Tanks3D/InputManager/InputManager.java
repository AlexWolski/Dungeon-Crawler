package Tanks3D.InputManager;

import Tanks3D.InputManager.PlayerControl.*;
import Tanks3D.Player;
import javax.swing.*;

import static java.awt.event.KeyEvent.*;
import static javax.swing.JComponent.*;

//Handles keyboard inputs.
public final class InputManager {
    //This class is non-instantiable
    private InputManager() {
    }

    public static void init(JComponent window, Player player) {
        //Get the input map from the window, which maps a key event to a string.
        InputMap inputMap = window.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        //Get the action map from the window, which maps a string to a class.
        ActionMap actionMap = window.getActionMap();

        //Key bindings for the first player.
        inputMap.put(KeyStroke.getKeyStroke(VK_W, 0, false), "w pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_W, 0, true), "w released");
        inputMap.put(KeyStroke.getKeyStroke(VK_S, 0, false), "s pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_S, 0, true), "s released");
        inputMap.put(KeyStroke.getKeyStroke(VK_A, 0, false), "a pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_A, 0, true), "a released");
        inputMap.put(KeyStroke.getKeyStroke(VK_D, 0, false), "d pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_D, 0, true), "d released");
        inputMap.put(KeyStroke.getKeyStroke(VK_SPACE, 0, false), "space pressed");

        //Map the classes for the player's controls.
        actionMap.put("w pressed", new Forward(player, true));
        actionMap.put("w released", new Forward(player, false));
        actionMap.put("s pressed", new Back(player, true));
        actionMap.put("s released", new Back(player, false));
        actionMap.put("a pressed", new Left(player, true));
        actionMap.put("a released", new Left(player, false));
        actionMap.put("d pressed", new Right(player, true));
        actionMap.put("d released", new Right(player, false));
        actionMap.put("space pressed", new Fire(player));
    }
}
