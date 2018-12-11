package Tanks3D;

import Tanks3D.Object.Entity.Entity;
import Tanks3D.Object.GameObject;
import Tanks3D.Object.Wall.Wall;

import java.util.ArrayList;

//A class to safely remove
public class ObjectManager {
    //The ArrayLists to remove the objects from.
    private static ArrayList<Entity> entityList;
    private static ArrayList<Wall> wallList;
    //The objects that will be removed.
    private static ArrayList<Entity> entitiesToRemove;
    private static ArrayList<Wall> wallsToRemove;
    //The objects that will be added.
    private static ArrayList<Entity> entitiesToAdd;
    private static ArrayList<Wall> wallsToAdd;

    //This class is non-instantiable.
    private ObjectManager() {
    }

    static void init(ArrayList<Entity> entities, ArrayList<Wall> walls) {
        entityList = entities;
        wallList = walls;
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
    }

    //Given an object, determine its type and add it to the list of objects to add.
    public static void add(GameObject objectToAdd) {
        if(objectToAdd instanceof Entity)
            entitiesToAdd.add((Entity) objectToAdd);
        else if(objectToAdd instanceof Wall)
            wallsToAdd.add((Wall) objectToAdd);
    }

    //Remove objects that need to be removed from the entity and wall lists.
    static void update() {
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

        //If there are entities to add, add them and clear the list.
        if(!entitiesToAdd.isEmpty()) {
            entityList.addAll(entitiesToAdd);
            entitiesToAdd.clear();
        }

        //If there are entities to add, add them and clear the list.
        if(!wallsToAdd.isEmpty()) {
            wallList.addAll(wallsToAdd);
            wallsToAdd.clear();
        }
    }
}
