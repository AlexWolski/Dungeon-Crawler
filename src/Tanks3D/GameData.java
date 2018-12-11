package Tanks3D;

import Tanks3D.DisplayComponents.Camera.Camera;
import Tanks3D.DisplayComponents.HUD;
import Tanks3D.DisplayComponents.Minimap;
import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.Entity.Player;

import java.util.ArrayList;

//A 'struct' to contain objects used in the game world. All of the members are public.
public final class GameData {
    //The player.
    public Player player;
    //An object that draws the world in 3D.
    public Camera camera;
    //The 'Level' object stores all of the data for the game world and draws it to the screen.
    public Level gameLevel;
    //A list of entities in the game. The first index is always playerController 1 and the second index is always playerController 2.
    public ArrayList<Entity> entityList;
    //Draws information on the screen.
    public HUD hud;
    //Draws the world in 2D.
    public Minimap minimap;
}