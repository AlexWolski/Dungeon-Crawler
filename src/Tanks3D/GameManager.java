package Tanks3D;

import Tanks3D.DisplayComponents.Camera.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.DisplayComponents.Minimap;
import Tanks3D.DisplayComponents.DisplayWindow;
import Tanks3D.InputManager.KeyboardManager;
import Tanks3D.InputManager.MouseManager;
import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Entity.Player;
import Tanks3D.Object.Entity.Round.Projectile;

import java.awt.*;
import java.util.ArrayList;

//Controls the entire game. The program enters from this class, which is an abstract.
public abstract class GameManager {
    //Constant settings.
    private final static String levelFile = "Underground Arena.txt";
    //Size of the display.
    private final static int defaultWidth = 1200;
    private final static int defaultHeight = 800;
    //How much space the title bar takes.
    private final static int titleBarHeight = 30;

    //The window that the game runs in and controls drawing of the screen.
    private final static DisplayWindow gameWindow;
    //'struct' that holds all of the data for the game world.
    private final static GameData gameData;

    //Determine if the game has ended and is restarting.
    private static boolean restarting;
    //Store the time when the game ended.
    private static long timeOfGameEnd;
    //How long it takes for the game to reset.
    private final static long restartTime = 3000;
    //Determine if the game is paused or not.
    private static boolean paused;

    //Store the time that the last frame was drawn so that 'deltaTime' can be calculated.
    private static long timeOfLastFrame;
    //Store the current time.
    private static long currentTime;
    //The time that elapsed since the last frame was drawn. This is used to run the game at a speed independent of FPS.
    private static long deltaTime;

    //Remove
    //Variable to count the number of frames run.
    private static int frames;
    //Timer for fps.
    private static long time;

    //Instantiate the game manager and update it.
    public static void main(String[] args) {
        while(true) {
            try {
                GameManager.update();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Initialize variables. The constructor is private to prevent any classes extending it and creating a new instance.
    static {
        //Instantiate local objects.
        gameData = new GameData();
        //Create the entity list.
        gameData.entityList = new ArrayList<>();
        //Load the level from a text file.
        gameData.gameLevel = new Level(levelFile, gameData.entityList);

        //Create and configure the JFrame. This JFrame will have three panels: the two players screens and a minimap.
        gameWindow = new DisplayWindow("Tanks 3D", new Dimension(defaultWidth, defaultHeight), titleBarHeight);

        //Initialize the game data elements.
        gameData.player = new Player(gameData.gameLevel.getPlayerSpawn());
        gameData.entityList.add(gameData.player);
        gameData.camera = new Camera(gameData, gameWindow.getScreenBuffer(), gameData.player.getPosition(), gameData.player.getzPos(), gameData.player.getAngle());
        gameData.hud = new HUD(gameWindow.getScreenBuffer());
        gameData.minimap = new Minimap(gameData, gameWindow.getMinimapBuffer());

        //A playerController object that controls the player.
        PlayerController playerController = new PlayerController(gameData.player);
        //Link the keyboard controls to the window.
        KeyboardManager.init(gameWindow.getPanel(), playerController);
        //Link the mouse controls to the window.
        MouseManager.init(gameWindow, playerController);

        //The game is not paused by default.
        paused = false;

        //Initialize the round object.
        Projectile.init(gameData.entityList);
        //Initialize the garbage collector.
        GarbageCollector.init(gameData.entityList, gameData.gameLevel.wallObjects);

        //Set the initial time.
        timeOfLastFrame = System.currentTimeMillis();

        //Remove
        //Set the timer for the FPS count.
        time = timeOfLastFrame;
    }

    private static void reset() {
        //Reset the level.
        gameData.gameLevel = new Level(levelFile, gameData.entityList);
        //Reset the entity list.
        gameData.entityList.clear();
        gameData.entityList.add(gameData.player);
        //Rest the round pool.
        Projectile.init(gameData.entityList);

        //Reset the player.
        gameData.player.resetPlayer();
    }

    //Get the current time, calculate the time elapsed since the last frame, and store it in 'deltaTime;.
    private static void updateDeltaTime() {
        //currentTimeMillis() is used instead of nanoTime() because its less expensive and more precise.
        currentTime = System.currentTimeMillis();
        deltaTime = currentTime - timeOfLastFrame;

        //If the game speed drops below 5 FPS, restrict deltaTime to prevent collision issues. This will slow down the game-play.
        if(deltaTime > 200)
            deltaTime = 200;

        //If the loop runs more than 1000 FPS, limit the game speed to 1000 updates per second. This is the fastest currentTimeMillis() can run.
        while (deltaTime == 0) {
            currentTime = System.currentTimeMillis();
            deltaTime = currentTime - timeOfLastFrame;
        }

        //Save the current time so that 'deltaTime' can be calculated next frame.
        timeOfLastFrame = currentTime;
    }

    private static void update() {
        //Update the time since the last frame.
        updateDeltaTime();

        //If the game is restarting and the restart time has passed, restart the game.
        if (restarting && currentTime - timeOfGameEnd > restartTime) {
            restarting = false;
            reset();
        }

        //Run the game if its not paused.
        if(!paused) {
            //Update the positions of all of the entities. Pass it the iterator in case the entity needs to remove itself from the list.
            for (Entity entity : gameData.entityList)
                entity.update(gameData, deltaTime);

            //Remove any unneeded objects.
            GarbageCollector.deleteObjects();
        }

        gameData.camera.draw();
        gameData.hud.draw(gameData.player.getMaxHealth(), gameData.player.getHealth());
        gameData.minimap.draw();

        //Draw the player's screen and the minimap.
        gameWindow.draw();

        //Remove
        //Print the FPS every second.
        if (currentTime - time > 1000) {
            System.out.println(frames);
            time = System.currentTimeMillis();
            frames = 0;
        }

        //Increment the frame count for the FPS.
        frames++;
    }

    public static void endGame(boolean win) {
        //If the game is not already restarting, restart it.
        if(!restarting) {
            //Save the current time and start counting down until the restart.
            timeOfGameEnd = System.currentTimeMillis();
            restarting = true;
            gameData.hud.setWin(win);
        }
    }

    public static void pause() {
        paused = true;
        MouseManager.pause();
        gameData.hud.setPaused(true);
    }

    public static void unPause() {
        paused = false;
        MouseManager.unPause();
        gameData.hud.setPaused(false);
    }
}