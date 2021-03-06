package DungeonCrawler;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

//A class for playing sounds.
public final class SoundManager {
    //A hash map that maps sound names to their directory.
    private static HashMap<String, String> audioClips;

    //This class is non-instantiable.
    private SoundManager() {
    }

    public static void init() {
        //Populate the hash map with valid sounds.
        audioClips = new HashMap<>();

        audioClips.put("Stairs", "resources/Sounds/Movement/Stairs.wav");
        audioClips.put("Footstep 1", "resources/Sounds/Movement/Footsteps/Footstep 1.wav");
        audioClips.put("Footstep 2", "resources/Sounds/Movement/Footsteps/Footstep 2.wav");
        audioClips.put("Footstep 3", "resources/Sounds/Movement/Footsteps/Footstep 3.wav");
        audioClips.put("Footstep 4", "resources/Sounds/Movement/Footsteps/Footstep 4.wav");
        audioClips.put("Footstep 5", "resources/Sounds/Movement/Footsteps/Footstep 5.wav");

        audioClips.put("Wall Hit", "resources/Sounds/Wall/Wall Hit.wav");
        audioClips.put("Wall Crumble", "resources/Sounds/Wall/Wall Crumble.wav");

        audioClips.put("Wood Hit 1", "resources/Sounds/Wood/Wood Hit 1.wav");
        audioClips.put("Wood Hit 2", "resources/Sounds/Wood/Wood Hit 2.wav");

        audioClips.put("Health", "resources/Sounds/Pickup/Health.wav");
        audioClips.put("Key", "resources/Sounds/Pickup/Key.wav");
        audioClips.put("Crossbow", "resources/Sounds/Pickup/Crossbow.wav");

        audioClips.put("Locked Door", "resources/Sounds/Action/Locked Door.wav");
        audioClips.put("Unlock", "resources/Sounds/Action/Unlock.wav");
        audioClips.put("Invalid", "resources/Sounds/Action/Invalid.wav");
    }

    public static void playSound(String soundFile) {
        try {
            if(audioClips.containsKey(soundFile)) {
                AudioStream audioStream = new AudioStream(new FileInputStream(audioClips.get(soundFile)));
                AudioPlayer.player.start(audioStream);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
