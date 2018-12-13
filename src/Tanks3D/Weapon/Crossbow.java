package Tanks3D.Weapon;

import Tanks3D.GameObject.Entity.Player;
import Tanks3D.GameObject.Entity.Projectile.Projectile;
import Tanks3D.Utilities.FastMath;

import java.awt.image.BufferedImage;

public class Crossbow extends Weapon {
    //The number of milliseconds it takes before the tank can attack again.
    private static final int attackCooldown = 100;
    //The last time the weapon was fired.
    private long lastAttack;
    //Determines if the player can attack again or not.
    private boolean reloading;

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

    }
}