package DungeonCrawler;

import DungeonCrawler.DisplayComponents.Camera.Camera;
import DungeonCrawler.DisplayComponents.HUD;
import DungeonCrawler.DisplayComponents.Minimap;
import DungeonCrawler.DisplayComponents.DisplayWindow;
import DungeonCrawler.GameObject.Update;
import DungeonCrawler.InputManager.KeyboardManager;
import DungeonCrawler.InputManager.MouseManager;
import DungeonCrawler.GameObject.Entity.Player;
import DungeonCrawler.GameObject.Entity.Projectile.Projectile;
import DungeonCrawler.Utilities.FastMath;

import java.awt.*;
import java.util.ArrayList;

//Controls the entire game. The program enters from this class, which is an abstract.
public abstract class GameManager {
    //The different levels.
    private final static String[] levelFiles;
    //The current level.
    private static int level = 0;
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
        //Populate the levels array.
        levelFiles = new String[] { "Level 1.txt", "Level 2.txt" };

        //Instantiate local objects.
        gameData = new GameData();
        //Instantiate the array lists in game data.
        gameData.wallList = new ArrayList<>();
        gameData.entityList = new ArrayList<>();
        gameData.usableList = new ArrayList<>();
        gameData.updateList = new ArrayList<>();

        //Initialize the math class.
        FastMath.init();
        //Initialize the sound class.
        SoundManager.init();
        //Initialize the garbage manager.
        ObjectManager.init(gameData);

        //Load the level from a text file.
        gameData.gameLevel = new Level(levelFiles[0]);

        //Create and configure the JFrame. This JFrame will have three panels: the two players screens and a minimap.
        gameWindow = new DisplayWindow("Tanks 3D", new Dimension(defaultWidth, defaultHeight), titleBarHeight);

        //Initialize the game data elements.
        gameData.player = new Player(gameData.gameLevel.getPlayerSpawn(), gameData.usableList);
        ObjectManager.add(gameData.player);
        gameData.camera = new Camera(gameData, gameWindow.getScreenBuffer(), gameData.player.getPosition(), gameData.player.getzPos(), gameData.player.getAngle());
        gameData.hud = new HUD(gameWindow.getScreenBuffer());
        gameData.minimap = new Minimap(gameData, gameWindow.getScreenBuffer(), new Dimension(gameWindow.getHeight()/3, gameWindow.getHeight()/3));

        //A playerController object that controls the player.
        PlayerController playerController = new PlayerController(gameData.player);
        //Link the keyboard controls to the window.
        KeyboardManager.init(gameWindow.getPanel(), playerController);
        //Link the mouse controls to the window.
        MouseManager.init(gameWindow, playerController);

        //Initialize the round object.
        Projectile.init();

        //The game is not paused by default.
        paused = false;

        //Set the initial time.
        timeOfLastFrame = System.currentTimeMillis();

        //Remove
        //Set the timer for the FPS count.
        time = timeOfLastFrame;
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
            //Update all of the objects that require updating.
            for (Update object : gameData.updateList)
                object.update(gameData, deltaTime);

            //Remove or add any objects.
            ObjectManager.update();
        }

        gameData.camera.draw();
        gameData.hud.draw(gameData.player.getMaxHealth(), gameData.player.getHealth());

        //Only draw the minimap if the game is not paused.
        if(!paused)
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

    private static void reset() {
        //Clear the entities in game data.
        ObjectManager.reset();
        //Reset the level.
        gameData.gameLevel = new Level(levelFiles[level]);
        //Add the player back to the entity and update list.
        ObjectManager.add(gameData.player);
        //Rest the round pool.
        Projectile.init();

        //Reset the player.
        gameData.player.setSpawnPoint(gameData.gameLevel.getPlayerSpawn());
        gameData.player.reset();
        //Reset the HUD.
        gameData.hud.setWin(false);
    }

    public static void endGame(boolean win) {
        //If the game is not already restarting, restart it.
        if(!restarting) {
            //Save the current time and start counting down until the restart.
            timeOfGameEnd = System.currentTimeMillis();
            restarting = true;
            gameData.hud.setWin(win);
            level = 0;
        }
    }

    public static void nextLevel() {
        level++;

        if(level >= levelFiles.length)
            endGame(true);
        else
            restarting = true;
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