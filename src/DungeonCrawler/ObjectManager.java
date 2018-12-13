package DungeonCrawler;

import DungeonCrawler.GameObject.Entity.Entity;
import DungeonCrawler.GameObject.GameObject;
import DungeonCrawler.GameObject.Update;
import DungeonCrawler.GameObject.Usable;
import DungeonCrawler.GameObject.Wall.Wall;

import java.util.ArrayList;

//A class to safely remove
public class ObjectManager {
    //A class containing the needed information.
    private static GameData gameData;
    //The objects that will be removed.
    private static ArrayList<Entity> entitiesToRemove;
    private static ArrayList<Wall> wallsToRemove;
    private static ArrayList<Update> objectsToStopUpdating;
    //The objects that will be added.
    private static ArrayList<Entity> entitiesToAdd;
    private static ArrayList<Wall> wallsToAdd;
    private static ArrayList<Update> objectsToStartUpdating;

    //This class is non-instantiable.
    private ObjectManager() {
    }

    static void init(GameData data) {
        gameData = data;

        entitiesToRemove = new ArrayList<>();
        wallsToRemove = new ArrayList<>();
        objectsToStopUpdating = new ArrayList<>();

        entitiesToAdd = new ArrayList<>();
        wallsToAdd = new ArrayList<>();
        objectsToStartUpdating = new ArrayList<>();
    }

    //Given an object, determine its type and add it to the list of objects to remove.
    public static void remove(GameObject objectToRemove) {
        gameData.usableList.remove(objectToRemove);

        if(objectToRemove instanceof Entity)
            entitiesToRemove.add((Entity) objectToRemove);
        else if(objectToRemove instanceof Wall)
            wallsToRemove.add((Wall) objectToRemove);

        if(objectToRemove instanceof Update)
            objectsToStopUpdating.add((Update) objectToRemove);
    }

    //Given an object, determine its type and add it to the list of objects to add.
    public static void add(GameObject objectToAdd) {
        if(objectToAdd instanceof Entity)
            entitiesToAdd.add((Entity) objectToAdd);
        else if(objectToAdd instanceof Wall)
            wallsToAdd.add((Wall) objectToAdd);

        if(objectToAdd instanceof Update)
            objectsToStartUpdating.add((Update) objectToAdd);
        if(objectToAdd instanceof Usable)
            gameData.usableList.add((Usable) objectToAdd);
    }

    //Remove objects that need to be removed from the entity and wall lists.
    static void update() {
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

        //If there are entities to start updating, start updating them and clear the list.
        if(!objectsToStartUpdating.isEmpty()) {
            gameData.updateList.addAll(objectsToStartUpdating);
            objectsToStartUpdating.clear();
        }

        //If there are entities to remove, remove them and clear the list.
        if(!entitiesToRemove.isEmpty()) {
            for (Entity entity : entitiesToRemove)
                gameData.entityList.remove(entity);
            entitiesToRemove.clear();
        }

        //If there are walls to remove, remove them and clear the list.
        if(!wallsToRemove.isEmpty()) {
            for (Wall wall : wallsToRemove)
                gameData.wallList.remove(wall);
            wallsToRemove.clear();
        }

        //If there are entities to stop updating, start stop them and clear the list.
        if(!objectsToStopUpdating.isEmpty()) {
            for (Update object : objectsToStopUpdating)
                gameData.updateList.remove(object);
            objectsToStopUpdating.clear();
        }
    }

    public static void reset() {
        entitiesToRemove.clear();
        wallsToRemove.clear();
        objectsToStopUpdating.clear();
        entitiesToAdd.clear();
        wallsToAdd.clear();
        objectsToStartUpdating.clear();

        gameData.wallList.clear();
        gameData.entityList.clear();
        gameData.updateList.clear();
        gameData.usableList.clear();
    }
}
