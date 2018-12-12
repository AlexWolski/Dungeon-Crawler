package Tanks3D;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

//A class for playing sounds.
public final class SoundManager {
    //A has hmap that maps strings to audio files.
    private static HashMap<String, InputStream> audioClips;

    //This class is non-instantiable.
    private SoundManager() {
    }

    public static void init() {
        //Instantiate the
        audioClips = new HashMap<>();
        
        try {
            audioClips.put("Footstep 1", new FileInputStream("resources/Sounds/Footsteps/Footstep 1.wav"));
            audioClips.put("Footstep 2", new FileInputStream("resources/Sounds/Footsteps/Footstep 2.wav"));
            audioClips.put("Footstep 3", new FileInputStream("resources/Sounds/Footsteps/Footstep 3.wav"));
            audioClips.put("Footstep 4", new FileInputStream("resources/Sounds/Footsteps/Footstep 4.wav"));
            audioClips.put("Footstep 5", new FileInputStream("resources/Sounds/Footsteps/Footstep 5.wav"));

            audioClips.put("Wall Hit", new FileInputStream("resources/Sounds/Wall/Wall Hit.wav"));
            audioClips.put("Wall Crumble", new FileInputStream("resources/Sounds/Wall/Wall Crumble.wav"));

            audioClips.put("Wood Hit 1", new FileInputStream("resources/Sounds/Wood/Wood Hit 1.wav"));
            audioClips.put("Wood Hit 2", new FileInputStream("resources/Sounds/Wood/Wood Hit 2.wav"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
