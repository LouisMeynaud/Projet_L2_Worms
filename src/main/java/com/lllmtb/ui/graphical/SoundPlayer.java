package com.lllmtb.ui.graphical;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


/**
 * gestionnaire de son pour le jeu.
 *
 * cette classe permet de jouer des bruitage et de la musique de fond
 */
public class SoundPlayer {
    private static Clip currentMusic; // la musique de fond en cours
    private static int musiqueVolume = 5; //le volume de la musique
    private static int soundEffectVolume = 75; // le volume des effet sonor

    /**
     * permet de jouer des effet sonor.
     * coupe l'effet precedent si on nouveaux est fait pour evitée la saturation
     *
     * @param key la cle du son que l'on veux jouer dans le soundManager
     */
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

    /**
     * permet de jouer une musique de fond.
     * couple la precedente
     *
     * @param key la cle de la musique a joeur dans soundManager
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

    /**
     *
     * @param clip le sont sur lequel on vas faire le changement de volume
     * @param volumePercent le pourcentage de sont voulu
     */
    private static void applyVolumeToClip(Clip clip, int volumePercent) {

        FloatControl controlSound = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volumePercent <= 0) {
                controlSound.setValue(-80.0f);
            } else {
        controlSound.setValue(40.0f * (volumePercent / 100.0f) - 40.0f); // marche avec des decibel
    }
    }

    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }
}
