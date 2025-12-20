package com.lllmtb.ui.graphical;

import javax.sound.sampled.Clip;

public class SoundPlayer {
    private static Clip currentMusic;

    public static void playSound(String key) {
        Clip clip = SoundManager.soundMap.get(key);
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0); //on le remet au debut
        clip.start();
    }

    public static void playMusique(String key) {
        if (currentMusic != null) {
            currentMusic.stop();
        }
        Clip clip = SoundManager.soundMap.get(key);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY); //repetition de la musique
            clip.start();

        }
    }
}
