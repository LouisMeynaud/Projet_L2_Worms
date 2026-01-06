package com.lllmtb.ui.graphical.Utilitaire.animation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.lllmtb.engine.logger.Logger;


public class Animation {
    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // Delay between frames
    private int currentFrame;               // Current frame being displayed
    private final int totalFrames;          // Total number of frames in animation
    private boolean stopped;                // Is the animation stopped
    private boolean playOnce = false;
    private boolean hasEnded = false;

    private final List<Frame> frames = new ArrayList<>();    // Arraylist of frames

    public Animation(BufferedImage[] frames, int frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        for (BufferedImage frame : frames) {
            addFrame(frame, frameDelay);
        }

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.totalFrames = this.frames.size();

    }

    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
    }

    public void stop() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
        currentFrame = 0;
        hasEnded = false;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
        this.hasEnded = false;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            Logger.logger.error("Invalid duration: {}", duration);
            throw new IllegalArgumentException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
        if (!stopped && !hasEnded) {
            frameCount++;

            if (frameCount >= frameDelay) {
                frameCount = 0;
                currentFrame += 1;

                if (currentFrame > totalFrames - 1) {
                    if (playOnce) {
                        currentFrame = totalFrames - 1;
                        hasEnded = true;
                    } else {
                        currentFrame = 0;
                    }
                }
            }
        }

    }

    public void setPlayOnce(boolean playOnce) {
        this.playOnce = playOnce;
    }

    public boolean hasEnded() {
        return hasEnded;
    }

}