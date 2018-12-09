package Tanks3D;

import Tanks3D.DisplayComponents.Minimap;
import Tanks3D.Object.Entity.Entity;

import java.util.ArrayList;

//A 'struct' to contain objects used in the game world. All of the members are public.
public final class GameData {
    //The 'Level' object stores all of the data for the game world and draws it to the screen.
    public Level gameLevel;
    //A list of entities in the game. The first index is always playerController 1 and the second index is always playerController 2.
    public ArrayList<Entity> entityList;

    //The object that controls the playerController's input and displays their screen.
    public PlayerController playerController;
    //The object that controls the minimap
    public Minimap minimap;
}