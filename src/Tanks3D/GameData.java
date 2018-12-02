package Tanks3D;

import Tanks3D.DisplayComponents.Minimap;
import Tanks3D.Object.Entity.Entity;

import java.util.ArrayList;

//A 'struct' to contain objects used in the game world. All of the members are public.
public final class GameData {
    //The 'Level' object stores all of the data for the game world and draws it to the screen.
    public Level gameLevel;
    //A list of entities in the game. The first index is always player 1 and the second index is always player 2.
    public ArrayList<Entity> entityList;

    //The object that controls the player's input and displays their screen.
    public Player player;
    //The object that controls the minimap
    public Minimap minimap;
}