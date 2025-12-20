package com.lllmtb.ui.graphical;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPlayer {
    private static Clip currentMusic;
    private static int musiqueVolume = 25;
    private static int soundEffectVolume = 75;

    public static void playSound(String key) {
        Clip clip = SoundManager.soundMap.get(key);
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0); //on le remet au debut
        applyVolumeToClip(clip, soundEffectVolume);
        clip.start();
    }

    public static void playMusique(String key) {
        if (currentMusic != null) {
            currentMusic.stop();
        }
        Clip clip = SoundManager.soundMap.get(key);
        if (clip != null) {
            currentMusic = clip;
            applyVolumeToClip(currentMusic, musiqueVolume);
            currentMusic.setFramePosition(0);
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY); //repetition de la musique
            currentMusic.start();

        }
    }
    public static int getMusiqueVolume() {
        return musiqueVolume;
    }

    public static int getSoundEffectVolume() {
        return soundEffectVolume;
    }

    public static void setMusiqueVolume(int volume) {
        musiqueVolume = volume;
        if (currentMusic != null) {
            applyVolumeToClip(currentMusic, musiqueVolume);
        }
    }

    public static void setSoundEffectVolume(int volume) {
        soundEffectVolume = volume;
    }

    private static void applyVolumeToClip(Clip clip, int volumePercent) {

        FloatControl controlSound = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volumePercent <= 0) {
                controlSound.setValue(-80.0f);
            } else {
        controlSound.setValue(40.0f * (volumePercent / 100.0f) - 40.0f);
    }
    }
}
