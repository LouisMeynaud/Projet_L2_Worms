package com.lllmtb.ui.graphical.animation;

import java.awt.image.BufferedImage;

/**
 * represente une image unique au sein d'une animation
 * chaque frame possete sa propre image et sa durée d'affichage
 */
public class Frame {

    private BufferedImage frame; //l'image a afficher
    private int duration; // le temps durant laquelle on vas l'afficher

    public Frame(BufferedImage frame, int duration) {
        this.frame = frame;
        this.duration = duration;
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public void setFrame(BufferedImage frame) {
        this.frame = frame;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}