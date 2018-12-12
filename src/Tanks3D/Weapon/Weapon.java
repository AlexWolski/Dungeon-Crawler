package Tanks3D.Weapon;

import Tanks3D.Object.Entity.Player;

import java.awt.image.BufferedImage;

public abstract class Weapon {
    protected final Player owner;

    public Weapon(Player owner) {
        this.owner = owner;
    }

    public abstract void attack();
    public abstract void draw(BufferedImage canvas);
}
