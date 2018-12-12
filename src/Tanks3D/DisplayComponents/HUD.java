package Tanks3D.DisplayComponents;

import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

//A Heads Up Display that shows the player's health, and lives.
public class HUD {
    //An buffer that get written to, then displayed on the screen.
    private final BufferedImage canvas;
    //The uncolored images for the HUD.
    private static BufferedImage defaultHealthIcon;
    //The images that are displayed when the game is over.
    private static BufferedImage winImage, loseImage, pausedImage;
    //The color the screen will be tinted when the game is paused.
    private static Color pausedColor;
    //The colored images.
    private BufferedImage healthIcon;
    //The position and dimensions of the image displayed when the game ends.
    private final Point messagePos;
    private final Dimension endGameImageDim;
    //The position of the status images.
    private final Point healthIconPos;
    //The position and dimension of the health bar.
    private final Point healthBarPos;
    private final Dimension healthBarDim;
    //The size of the health and life icons.
    private static final int iconSize = 50;
    //The distance between the icons and the side of the screen.
    private static final int iconGap = 20;
    //Booleans to control messages displayed during different states of the game.
    private boolean win, lose, paused;


    static {
        defaultHealthIcon = Image.load("resources/HUD/Health Icon.png");
        pausedColor = new Color(0, 0, 0, 150);

        //Load the messages for when the game is won or lost.
        winImage = Image.load("resources/HUD/Win.png");
        loseImage = Image.load("resources/HUD/Lose.png");
        pausedImage = Image.load("resources/HUD/Paused.png");
    }

    public HUD(BufferedImage canvas) {
        this.canvas = canvas;
        this.healthBarDim = new Dimension(canvas.getWidth()/2, canvas.getWidth()/15);

        healthIcon = Image.copy(defaultHealthIcon);

        //Calculate the position of the health and life.
        healthIconPos = new Point(iconGap, iconGap);
        healthBarPos = new Point(healthIconPos.x + iconGap + iconSize, healthIconPos.y + (iconSize - healthBarDim.height)/2);

        //Calculate the dimension of the end of game message.
        endGameImageDim = new Dimension();
        endGameImageDim.width = (int)(canvas.getWidth() * 0.75);
        endGameImageDim.height = (int)((double)winImage.getHeight()/winImage.getWidth() * endGameImageDim.width);
        //Calculate the position where messages will be drawn.
        messagePos = new Point();
        messagePos.x = canvas.getWidth()/2 - endGameImageDim.width/2;
        messagePos.y = canvas.getHeight()/2 - endGameImageDim.height/2;

        //By default, the game is in a normal state.
        win = lose = paused = false;
    }

    //Draw the player's status.
    public void draw(int maxHealth, int health) {
        Graphics2D graphic = canvas.createGraphics();

        //If the game is not paused, draw the player's stats.
        if(!paused) {
            //Draw the health icon.
            graphic.drawImage(healthIcon, healthIconPos.x, healthIconPos.y, iconSize, iconSize, null);

            //Calculate how wide the health bar is based on the current and maximum health.
            int healthWidth = (int) (healthBarDim.width * (health / (double) maxHealth));

            //Draw the health bar background.
            graphic.setPaint(Color.darkGray);
            graphic.fillRect(healthBarPos.x, healthBarPos.y, healthBarDim.width, healthBarDim.height);

            //Set the color for the health bar.
            graphic.setPaint(Color.RED);

            graphic.fillRect(healthBarPos.x + (healthBarDim.width - healthWidth), healthBarPos.y, healthWidth, healthBarDim.height);

            //Draw the max health.
            graphic.setStroke(new BasicStroke(2));
            graphic.setColor(Color.white);
            graphic.drawRect(healthBarPos.x, healthBarPos.y, healthBarDim.width, healthBarDim.height);

            //If the playerController won the game, display the winning message.
            if (win)
                graphic.drawImage(winImage, messagePos.x, messagePos.y, endGameImageDim.width, endGameImageDim.height, null);
                //If the playerController lost the game, display the losing message.
            else if (lose)
                graphic.drawImage(loseImage, messagePos.x, messagePos.y, endGameImageDim.width, endGameImageDim.height, null);
        }
        //If the game is paused, draw a dark rectangle and the paused message.
        else {
            //Draw the dark rectangle.
            graphic.setColor(pausedColor);
            graphic.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            //Draw the 'paused' message.
            graphic.drawImage(pausedImage, messagePos.x, messagePos.y, endGameImageDim.width, endGameImageDim.height, null);
        }
    }

    //Set the states of the game.
    public void setWin(boolean win) {
        this.win = win;
    }
    public void setLose(boolean lose) {
        this.lose = lose;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}