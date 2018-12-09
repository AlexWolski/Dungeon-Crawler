package Tanks3D;

import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.GameObject;
import Tanks3D.Object.Wall.Wall;

import java.util.ArrayList;

//A class to safely remove
public class GarbageCollector {
    //The ArrayLists to remove the objects from.
    private static ArrayList<Entity> entityList;
    private static ArrayList<Wall> wallList;
    //The objects that will be removed.
    private static ArrayList<Entity> entitiesToRemove;
    private static ArrayList<Wall> wallsToRemove;

    //This class is non-instantiable.
    private GarbageCollector() {
    }

    static void init(ArrayList<Entity> entities, ArrayList<Wall> walls) {
        entityList = entities;
        wallList = walls;
        entitiesToRemove = new ArrayList<>();
        wallsToRemove = new ArrayList<>();
    }

    //Given an object, determine its type and add it to the list of objects to remove.
    public static void remove(GameObject objectToRemove) {
        if(objectToRemove instanceof Entity)
            entitiesToRemove.add((Entity) objectToRemove);
        else if(objectToRemove instanceof Wall)
            wallsToRemove.add((Wall) objectToRemove);
    }

    //Remove objects that need to be removed from the entity and wall lists.
    static void deleteObjects() {
        //If there are entities to remove, remove them and clear the list.
        if(!entitiesToRemove.isEmpty()) {
            for (Entity entity : entitiesToRemove)
                entityList.remove(entity);
            entitiesToRemove.clear();
        }

        //If there are entities to remove, remove them and clear the list.
        if(!wallsToRemove.isEmpty()) {
            for (Wall wall : wallsToRemove)
                wallList.remove(wall);
            wallsToRemove.clear();
        }
    }
}
