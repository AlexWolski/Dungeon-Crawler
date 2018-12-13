package Tanks3D.Weapon;

import Tanks3D.GameObject.Entity.Player;

import java.awt.image.BufferedImage;

public abstract class Weapon {
    final Player owner;

    Weapon(Player owner) {
        this.owner = owner;
    }

    public abstract void attack();
    public abstract void draw(BufferedImage canvas);
}
