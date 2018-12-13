package Tanks3D;

import Tanks3D.DisplayComponents.Camera.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.DisplayComponents.Minimap;
import Tanks3D.GameObject.Entity.Entity;
import Tanks3D.GameObject.Entity.Player;
import Tanks3D.GameObject.Update;
import Tanks3D.GameObject.Usable;
import Tanks3D.GameObject.Wall.Wall;

import java.util.ArrayList;

//A 'struct' to contain objects used in the game world. All of the members are public.
public final class GameData {
    //The player.
    public Player player;
    //An object that draws the world in 3D.
    public Camera camera;
    //The 'Level' object stores all of the data for the game world and draws it to the screen.
    public Level gameLevel;
    //A list of walls in the game.
    public ArrayList<Wall> wallList;
    //A list of entities in the game.
    public ArrayList<Entity> entityList;
    //A list of objects that the player can interact with the 'use' button.
    public ArrayList<Usable> usableList;
    //A list of objects that need to be updated.
    public ArrayList<Update> updateList;
    //Draws information on the screen.
    public HUD hud;
    //Draws the world in 2D.
    public Minimap minimap;
}