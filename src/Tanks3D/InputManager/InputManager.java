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

    public static void init(JComponent window, Player player1, Player player2) {
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

        //Key bindings for the second player.
        inputMap.put(KeyStroke.getKeyStroke(VK_UP, 0, false), "up pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_UP, 0, true), "up released");
        inputMap.put(KeyStroke.getKeyStroke(VK_DOWN, 0, false), "down pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_DOWN, 0, true), "down released");
        inputMap.put(KeyStroke.getKeyStroke(VK_LEFT, 0, false), "left pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_LEFT, 0, true), "left released");
        inputMap.put(KeyStroke.getKeyStroke(VK_RIGHT, 0, false), "right pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_RIGHT, 0, true), "right released");
        inputMap.put(KeyStroke.getKeyStroke(VK_ENTER, 0, false), "enter pressed");

        //Map the classes for player 1's controls.
        actionMap.put("w pressed", new Forward(player1, true));
        actionMap.put("w released", new Forward(player1, false));
        actionMap.put("s pressed", new Back(player1, true));
        actionMap.put("s released", new Back(player1, false));
        actionMap.put("a pressed", new Left(player1, true));
        actionMap.put("a released", new Left(player1, false));
        actionMap.put("d pressed", new Right(player1, true));
        actionMap.put("d released", new Right(player1, false));
        actionMap.put("space pressed", new Fire(player1));

        //Map the classes for player 2's controls.
        actionMap.put("up pressed", new Forward(player2, true));
        actionMap.put("up released", new Forward(player2, false));
        actionMap.put("down pressed", new Back(player2, true));
        actionMap.put("down released", new Back(player2, false));
        actionMap.put("left pressed", new Left(player2, true));
        actionMap.put("left released", new Left(player2, false));
        actionMap.put("right pressed", new Right(player2, true));
        actionMap.put("right released", new Right(player2, false));
        actionMap.put("enter pressed", new Fire(player2));
    }
}
