package com.lllmtb.ui.graphical.Utilitaire;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.lllmtb.engine.logger.Logger;

public class SoundManager {
    public static final Map<String, Clip> soundMap = new HashMap<>();

    /**
    * Method for loading all the sounds.
    */
    public static void  load() {
        soundMap.put("BazookaExplosion", createClip("BazookaExplosion"));
        soundMap.put("HealingBazookaExplosion", createClip("HealingBazookaExplosion"));
        soundMap.put("MusiqueGame1", createClip("MusiqueGame1"));
        soundMap.put("MusiqueGame2", createClip("MusiqueGame2"));
        soundMap.put("MusiqueGame3", createClip("MusiqueGame3"));
        soundMap.put("BAZOOKAShot", createClip("BAZOOKAShot"));
        soundMap.put("HEALINGBAZOOKAShot", createClip("HEALINGBAZOOKAShot"));
        soundMap.put("GUNShot", createClip("GUNShot"));
        soundMap.put("SHOTGUNShot", createClip("SHOTGUNShot"));
        soundMap.put("ELECTRONCANONShot", createClip("ELECTRONCANONShot"));
        soundMap.put("CatConcreteExplosion", createClip("CatConcreteExplosion"));

    }

    /**

    * Method for creating a clip from a file.

    * 1: Retrieve the sound from a folder
    * 2: Create an empty container (the clip)
    * 3: Place the sound in the container
    * @param nom Name of the music file to add
    * @return The music clip
    */
    private static Clip createClip(String nom) {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/" + nom + ".wav")); //lie le fichier audio (ouvre un "flux")
            Clip clip = AudioSystem.getClip(); //crée une "place" pour le son sur la Ram
            clip.open(sound); //place le sont dans la place crée
            return clip;
        } catch (Exception e) {
            Logger.logger.error("Error while loading sound : {} : ", nom, e);
            return null;
        }
    }
}
