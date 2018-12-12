package Tanks3D;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        audioClips.put("Footstep 1", "resources/Sounds/Footsteps/Footstep 1.wav");
        audioClips.put("Footstep 2", "resources/Sounds/Footsteps/Footstep 2.wav");
        audioClips.put("Footstep 3", "resources/Sounds/Footsteps/Footstep 3.wav");
        audioClips.put("Footstep 4", "resources/Sounds/Footsteps/Footstep 4.wav");
        audioClips.put("Footstep 5", "resources/Sounds/Footsteps/Footstep 5.wav");

        audioClips.put("Wall Hit", "resources/Sounds/Wall/Wall Hit.wav");
        audioClips.put("Wall Crumble", "resources/Sounds/Wall/Wall Crumble.wav");

        audioClips.put("Wood Hit 1", "resources/Sounds/Wood/Wood Hit 1.wav");
        audioClips.put("Wood Hit 2", "resources/Sounds/Wood/Wood Hit 2.wav");
    }

    public static void playSound(String soundFile) {
        try {
            AudioStream audioStream = new AudioStream(new FileInputStream(audioClips.get(soundFile)));
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
