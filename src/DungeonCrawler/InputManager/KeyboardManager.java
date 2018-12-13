package DungeonCrawler.InputManager;

import DungeonCrawler.InputManager.KeyboardControls.*;
import DungeonCrawler.PlayerController;

import javax.swing.*;

import static java.awt.event.KeyEvent.*;
import static javax.swing.JComponent.*;

//Handles keyboard inputs.
public final class KeyboardManager {
    //This class is non-instantiable
    private KeyboardManager() {
    }

    public static void init(JComponent window, PlayerController playerController) {
        //Get the input map from the window, which maps a key event to a string.
        InputMap inputMap = window.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        //Get the action map from the window, which maps a string to a class.
        ActionMap actionMap = window.getActionMap();

        //Key bindings for the controls.
        inputMap.put(KeyStroke.getKeyStroke(VK_W, 0, false), "w pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_W, 0, true), "w released");
        inputMap.put(KeyStroke.getKeyStroke(VK_S, 0, false), "s pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_S, 0, true), "s released");
        inputMap.put(KeyStroke.getKeyStroke(VK_A, 0, false), "a pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_A, 0, true), "a released");
        inputMap.put(KeyStroke.getKeyStroke(VK_D, 0, false), "d pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_D, 0, true), "d released");
        inputMap.put(KeyStroke.getKeyStroke(VK_LEFT, 0, false), "left pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_LEFT, 0, true), "left released");
        inputMap.put(KeyStroke.getKeyStroke(VK_RIGHT, 0, false), "right pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_RIGHT, 0, true), "right released");
        inputMap.put(KeyStroke.getKeyStroke(VK_SPACE, 0, false), "space pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_SPACE, 0, true), "space released");
        inputMap.put(KeyStroke.getKeyStroke(VK_E, 0, false), "e pressed");
        inputMap.put(KeyStroke.getKeyStroke(VK_ESCAPE, 0, false), "escape pressed");

        //Map the classes to the controls.
        actionMap.put("w pressed", new Forward(playerController, true));
        actionMap.put("w released", new Forward(playerController, false));
        actionMap.put("s pressed", new Back(playerController, true));
        actionMap.put("s released", new Back(playerController, false));
        actionMap.put("a pressed", new Left(playerController, true));
        actionMap.put("a released", new Left(playerController, false));
        actionMap.put("d pressed", new Right(playerController, true));
        actionMap.put("d released", new Right(playerController, false));
        actionMap.put("left pressed", new LookLeft(playerController, true));
        actionMap.put("left released", new LookLeft(playerController, false));
        actionMap.put("right pressed", new LookRight(playerController, true));
        actionMap.put("right released", new LookRight(playerController, false));
        actionMap.put("space pressed", new Fire(playerController, true));
        actionMap.put("space released", new Fire(playerController, false));
        actionMap.put("e pressed", new Use(playerController));
        actionMap.put("escape pressed", new Pause());
    }
}