package com.lllmtb.ui.graphical.Utilitaire;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


/**
 * * Class for playing sounds and music in the game.
 */
public class SoundPlayer {
    private static volatile Clip currentMusic; // the current music clip being played
    private static volatile int musiqueVolume = 5; //the volume of the music
    private static volatile int soundEffectVolume = 75; // the volume of the sound effects

    /**
     * Method to play a sound effect.
     * @param key the key of the sound to play in soundManager
     */
    public static void playSound(String key) {
        Clip clip = SoundManager.soundMap.get(key);
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0); //rewind to the beginning
        applyVolumeToClip(clip, soundEffectVolume);
        clip.start();
    }

    /**
     * Method to play background music.
     * @param key the key of the music to play in soundManager
     */
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

    /**
     * Gets the current music.
     * @return the current music as a clip.
     */
    public static Clip getCurrentMusic() {
        return currentMusic;
    }

    /**
     * Gets the current music volume.
     * @return the music volume as an integer.
     */
    public static int getMusiqueVolume() {
        return musiqueVolume;
    }
    /**
     * Gets the current sound effect volume.
     * @return the sound effect volume as an integer.
     */
    public static int getSoundEffectVolume() {
        return soundEffectVolume;
    }

    /**
     * Sets the music volume.
     * @param volume the desired music volume as an integer.
     */
    public static void setMusiqueVolume(int volume) {
        musiqueVolume = volume;
        if (currentMusic != null) {
            applyVolumeToClip(currentMusic, musiqueVolume);
        }
    }

    /**
     * Sets the sound effect volume.
     * @param volume the desired sound effect volume as an integer.
     */
    public static void setSoundEffectVolume(int volume) {
        soundEffectVolume = volume;
    }

    /**
     * Applies the specified volume to the given clip.
     * @param clip the Clip object to adjust volume for.
     * @param volumePercent the desired volume level as a percentage (0-100).
     */
    private static void applyVolumeToClip(Clip clip, int volumePercent) {

        FloatControl controlSound = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volumePercent <= 0) {
                controlSound.setValue(-80.0f);
            } else {
        controlSound.setValue(40.0f * (volumePercent / 100.0f) - 40.0f); // marche avec des decibel
    }
    }
    /**
     * Stops the currently playing music.
     */
    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }
}
