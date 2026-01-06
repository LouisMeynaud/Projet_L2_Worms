package com.lllmtb.ui.graphical.animation;

import java.awt.image.BufferedImage;

/**
 * Represents a single image within an animation.
 * Each frame has its own image and display duration.
 */
public class Frame {

    private BufferedImage frame;
    private int duration;

    /**
     * Constructor to create a new frame.
     * @param frame The BufferedImage of the frame.
     * @param duration The display duration of the frame in ticks.
     */
    public Frame(BufferedImage frame, int duration) {
        this.frame = frame;
        this.duration = duration;
    }

    /**
     * Returns the image of the frame.
     * @return The BufferedImage.
     */
    public BufferedImage getFrame() {
        return frame;
    }

    /**
     * Sets the image of the frame.
     * @param frame The new BufferedImage.
     */
    public void setFrame(BufferedImage frame) {
        this.frame = frame;
    }

    /**
     * Returns the display duration of the frame.
     * @return The duration in ticks.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the display duration of the frame.
     * @param duration The new duration in ticks.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

}