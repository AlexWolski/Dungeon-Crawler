package DungeonCrawler.GameObject.Wall.Door;

import DungeonCrawler.GameObject.Entity.Player;
import DungeonCrawler.SoundManager;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class LockedDoor extends Door {
    private String unlockedImage;

    protected boolean locked;

    public LockedDoor(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String lockedImage, String unlockedImage, double swingAngle, Color textureColor) {
        super(point1, point2, seeThrough, characterCollidable, projectileCollidable, lockedImage, swingAngle, textureColor);

        this.unlockedImage = unlockedImage;

        locked = true;
    }

    public void use(Player player) {
        if(locked) {
            if(player.getKey() != null) {
                locked = false;
                setTexture(unlockedImage);
                SoundManager.playSound("Unlock");
            }
            else
                SoundManager.playSound("Locked Door");
        }
        else
            super.use(player);
    }
}
