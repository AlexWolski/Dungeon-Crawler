package DungeonCrawler.GameObject;

import DungeonCrawler.GameData;

//Game objects that require a regular update.
public interface Update {
    void update(GameData gamedata, double deltaTime);
}
