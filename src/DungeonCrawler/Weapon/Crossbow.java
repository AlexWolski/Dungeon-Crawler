package DungeonCrawler.Weapon;

import DungeonCrawler.GameObject.Entity.Player;
import DungeonCrawler.GameObject.Entity.Projectile.Projectile;
import DungeonCrawler.Utilities.FastMath;
import DungeonCrawler.Utilities.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crossbow extends Weapon {
    //An array of images of the crossbow.
    private static final BufferedImage[] images;
    //The amount to scale the image.
    private static double scale = 2;

    //The number of milliseconds it takes before the tank can attack again.
    private static final int attackCooldown = 100;
    //The last time the weapon was fired.
    private long lastAttack;
    //Determines if the player can attack again or not.
    private boolean reloading;

    static {
        images = new BufferedImage[1];
        images[0] = Image.load("resources/Weapons/Crossbow/Crossbow 2/Ready.png");
    }

    public Crossbow(Player owner) {
        super(owner);
    }

    public void attack() {
        //If the tank is not reloading, attack.
        if(!reloading) {
            //Set the last time the weapon was used.
            lastAttack = System.currentTimeMillis();
            reloading = true;

            //Calculate the distance to spawn the round.
            double distance = 10;
            //Calculate the x and y position to spawn the round based on the player's position and directionAngle.
            double xPos = owner.position.x + distance * FastMath.sin(owner.getViewAngle().getValue());
            double yPos = owner.position.y + distance * FastMath.cos(owner.getViewAngle().getValue());
            //Create the round and add it to the entity list.
            Projectile.newArmorPiercing(xPos, yPos, owner.getWeaponHeight(), owner.getViewAngle().getValue(), owner);
        }
        //If the tank is reloading, check if the reload time is up. If it is, set reloading to false.
        else if(System.currentTimeMillis() >= lastAttack + attackCooldown) {
            reloading = false;
        }
    }

    public void draw(BufferedImage canvas) {
        Graphics2D graphic = canvas.createGraphics();

        int width = (int)(images[0].getWidth() * scale);
        int height = (int)(images[0].getHeight() * scale);
        graphic.drawImage(images[0], canvas.getWidth()/2 - width/2, canvas.getHeight() - height, width, height, null);
    }
}