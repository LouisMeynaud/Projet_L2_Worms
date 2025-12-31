package com.lllmtb.ui.graphical;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.lllmtb.engine.logger.Logger;

public class SoundManager {
    public static final Map<String, Clip> soundMap = new HashMap<>();

    /**
     * place dans le sound map les different son du jeu a charger.
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

    }

    /**
     * methode permetant de crée a partir d'un fichier un clip.
     * 1: recuperation du sons depuis un dossier
     * 2 : creation d'un conteneur vide (le clip)
     * 3 : placement du sont dans le conteneur
     *
     * @param nom nom du fichier de la musique a ajouter
     * @return le clip de la musique
     */
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
