package DungeonCrawler.GameObject;

import DungeonCrawler.GameObject.Entity.Player;

//Game objects that are intractable using the 'use' key.
public interface Usable {
    void use(Player player);
}
