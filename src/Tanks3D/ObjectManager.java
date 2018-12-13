package Tanks3D;

import Tanks3D.GameObject.Entity.Entity;
import Tanks3D.GameObject.GameObject;
import Tanks3D.GameObject.Update;
import Tanks3D.GameObject.Usable;
import Tanks3D.GameObject.Wall.Wall;

import java.util.ArrayList;

//A class to safely remove
public class ObjectManager {
    //A class containing the needed information.
    private static GameData gameData;
    //The objects that will be removed.
    private static ArrayList<Entity> entitiesToRemove;
    private static ArrayList<Wall> wallsToRemove;
    //The objects that will be added.
    private static ArrayList<Entity> entitiesToAdd;
    private static ArrayList<Wall> wallsToAdd;

    //This class is non-instantiable.
    private ObjectManager() {
    }

    static void init(GameData data) {
        gameData = data;
        entitiesToRemove = new ArrayList<>();
        wallsToRemove = new ArrayList<>();
        entitiesToAdd = new ArrayList<>();
        wallsToAdd = new ArrayList<>();
    }

    //Given an object, determine its type and add it to the list of objects to remove.
    public static void remove(GameObject objectToRemove) {
        if(objectToRemove instanceof Entity)
            entitiesToRemove.add((Entity) objectToRemove);
        else if(objectToRemove instanceof Wall)
            wallsToRemove.add((Wall) objectToRemove);

        gameData.usableList.remove(objectToRemove);
        gameData.updateList.remove(objectToRemove);
    }

    //Given an object, determine its type and add it to the list of objects to add.
    public static void add(GameObject objectToAdd) {
        if(objectToAdd instanceof Entity)
            entitiesToAdd.add((Entity) objectToAdd);
        else if(objectToAdd instanceof Wall)
            wallsToAdd.add((Wall) objectToAdd);

        if(objectToAdd instanceof Usable)
            gameData.usableList.add((Usable) objectToAdd);
        if(objectToAdd instanceof Update)
            gameData.updateList.add((Update) objectToAdd);
    }

    //Remove objects that need to be removed from the entity and wall lists.
    static void update() {
        //If there are entities to remove, remove them and clear the list.
        if(!entitiesToRemove.isEmpty()) {
            for (Entity entity : entitiesToRemove)
                gameData.entityList.remove(entity);
            entitiesToRemove.clear();
        }

        //If there are entities to remove, remove them and clear the list.
        if(!wallsToRemove.isEmpty()) {
            for (Wall wall : wallsToRemove)
                gameData.wallList.remove(wall);
            wallsToRemove.clear();
        }

        //If there are entities to add, add them and clear the list.
        if(!entitiesToAdd.isEmpty()) {
            gameData.entityList.addAll(entitiesToAdd);
            entitiesToAdd.clear();
        }

        //If there are entities to add, add them and clear the list.
        if(!wallsToAdd.isEmpty()) {
            gameData.wallList.addAll(wallsToAdd);
            wallsToAdd.clear();
        }
    }
}
