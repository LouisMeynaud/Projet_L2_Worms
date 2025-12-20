package com.lllmtb.ui.graphical.animation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {

    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // Delay between frames
    private int currentFrame;               // Current frame being displayed
    private int totalFrames;                // Total number of frames in animation

    private boolean stopped;                // Is the animation stopped

    private boolean playOnce = false;
    private boolean hasEnded = false;

    private List<Frame> frames = new ArrayList<Frame>();    // Arraylist of frames

    public Animation(BufferedImage[] frames, int frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
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

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }

    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.size() == 0) {
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
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
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