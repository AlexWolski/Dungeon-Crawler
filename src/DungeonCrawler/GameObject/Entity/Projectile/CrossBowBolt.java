package DungeonCrawler.GameObject.Entity.Projectile;

import DungeonCrawler.GameObject.Entity.Player;
import DungeonCrawler.Utilities.Image;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CrossBowBolt extends Projectile {
    private final static BufferedImage[] sprites;
    private final static Color imageColor = Color.white;
    private final static int speed = 60;
    private final static int damage = 25;

    //Load the images for the round.
    static {
        sprites = new BufferedImage[1];
        sprites[0] = Image.load("resources/Rounds/Armor Piercing.png");
    }

    public CrossBowBolt(Point2D.Double position, int zPos, double angle, Player owner) {
        super(position, zPos, angle, speed, damage, sprites, imageColor, owner);
    }
}
