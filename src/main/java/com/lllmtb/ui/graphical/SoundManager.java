package com.lllmtb.ui.graphical;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.lllmtb.engine.logger.Logger;

public class SoundManager {
    public static final Map<String, Clip> soundMap = new HashMap<>();

    public static void  load() {
        soundMap.put("Explosion", createClip("Explosion"));
        soundMap.put("MusiqueGame1", createClip("MusiqueGame1"));
        soundMap.put("BAZOOKAShot", createClip("BAZOOKAShot"));
        soundMap.put("GUNShot", createClip("GUNShot"));

    }

    private static Clip createClip(String nom) {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sound/" + nom + ".wav")); //lie le fichier audio (ouvre un "flux")
            Clip clip = AudioSystem.getClip(); //crée une "place" pour le son sur la Ram
            clip.open(sound); //place le sont dans la place crée
            return clip;
        } catch (Exception e) {
            Logger.logger.error("Error while loading sound : " + nom + " : ", e);
            return null;
        }
    }
}
