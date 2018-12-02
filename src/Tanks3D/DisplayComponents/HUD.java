package Tanks3D.DisplayComponents;

import Tanks3D.Utilities.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

//A Heads Up Display that shows the player's tank, health, and lives.
public class HUD {
    //An buffer that get written to, then displayed on the screen.
    private final BufferedImage canvas;
    //The gradient used for the health bar.
    private final GradientPaint gradient;
    //The uncolored images for the HUD.
    private static BufferedImage defaultHealthIcon, defaultLifeIcon;
    //The images that are displayed when the game is over.
    private static BufferedImage winImage, loseImage;
    //The colored images.
    private BufferedImage healthIcon, lifeIcon;
    //The position and dimensions of the image displayed when the game ends.
    private final Point endGameImagePos;
    private final Dimension endGameImageDim;
    //The position of the status images.
    private final Point healthIconPos, lifeIconPos;
    //The position and dimension of the health bar.
    private final Point healthBarPos;
    private final Dimension healthBarDim;
    //The size of the health and life icons.
    private static final int iconSize = 50;
    //The distance between the icons and the side of the screen.
    private static final int iconGap = 20;
    //The distance between each life icon. Can be positive or negative depending on if its on the left or right.
    private final int lifeIconGap;

    static {
        defaultHealthIcon = Image.load("resources/HUD/Health Icon.png");
        defaultLifeIcon = Image.load("resources/HUD/Life Icon.png");

        //Load the messages for when the game is won or lost.
        winImage = Image.load("resources/Game End/Win.png");
        loseImage = Image.load("resources/Game End/Lose.png");
    }

    public HUD(BufferedImage canvas, Color hudColor) {
        this.canvas = canvas;
        this.healthBarDim = new Dimension(canvas.getWidth()/2, canvas.getWidth()/15);

        healthIcon = Image.copy(defaultHealthIcon);
        lifeIcon = Image.copy(defaultLifeIcon);

        //Color the images.
        Tanks3D.Utilities.Image.tintImage(healthIcon, hudColor);
        Tanks3D.Utilities.Image.tintImage(lifeIcon, hudColor);

        //Calculate the position of the health and life.
        gradient = new GradientPaint(0, 0, Color.darkGray, 500, 0, hudColor);
        healthIconPos = new Point(iconGap, iconGap);
        healthBarPos = new Point(healthIconPos.x + iconGap + iconSize, healthIconPos.y + (iconSize - healthBarDim.height)/2);
        lifeIconPos = new Point(iconGap, 2*iconGap + iconSize);
        lifeIconGap = iconSize + iconGap;

        //Calculate the dimension of the end of game message.
        endGameImageDim = new Dimension();
        endGameImageDim.width = (int)(canvas.getWidth() * 0.75);
        endGameImageDim.height = (int)((double)winImage.getHeight()/winImage.getWidth() * endGameImageDim.width);
        //Calculate the position of the end of game message.
        endGameImagePos = new Point();
        endGameImagePos.x = canvas.getWidth()/2 - endGameImageDim.width/2;
        endGameImagePos.y = canvas.getHeight()/2 - endGameImageDim.height/2;
    }

    //Draw the player's tank and status.
    public void draw(int maxHealth, int health, int lives, boolean win, boolean lose, boolean alive) {
        Graphics2D graphic = canvas.createGraphics();

        //Draw the health icon.
        graphic.drawImage(healthIcon, healthIconPos.x, healthIconPos.y, iconSize, iconSize, null);

        //Draw the hearts.
        for (int i = 0; i < lives; i++)
            graphic.drawImage(lifeIcon, lifeIconPos.x + i * lifeIconGap, lifeIconPos.y, iconSize, iconSize, null);

        //Calculate how wide the health bar is based on the current and maximum health.
        int healthWidth = (int)(healthBarDim.width * (health/(double)maxHealth));

        //Draw the health bar background.
        graphic.setPaint(Color.darkGray);
        graphic.fillRect(healthBarPos.x, healthBarPos.y, healthBarDim.width, healthBarDim.height);

        //Set the gradient for the health bar.
        graphic.setPaint(gradient);

        graphic.fillRect(healthBarPos.x + (healthBarDim.width - healthWidth), healthBarPos.y, healthWidth, healthBarDim.height);

        //Draw the max health.
        graphic.setStroke(new BasicStroke(2));
        graphic.setColor(Color.white);
        graphic.drawRect(healthBarPos.x, healthBarPos.y, healthBarDim.width, healthBarDim.height);

        //If the player won the game, display the winning message.
        if(win)
            graphic.drawImage(winImage, endGameImagePos.x, endGameImagePos.y, endGameImageDim.width, endGameImageDim.height, null);
        //If the player lost the game, display the losing message.
        else if(lose)
            graphic.drawImage(loseImage, endGameImagePos.x, endGameImagePos.y, endGameImageDim.width, endGameImageDim.height, null);
    }
}