package Tanks3D.GameObject;

import Tanks3D.GameData;

//Game objects that require a regular update.
public interface Update {
    void update(GameData gamedata, double deltaTime);
}
