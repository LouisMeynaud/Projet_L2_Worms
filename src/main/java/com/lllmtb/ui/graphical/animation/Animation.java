package com.lllmtb.ui.graphical.animation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.lllmtb.engine.logger.Logger;


/**
 * Class representing an animation composed of multiple frames.
 * Allows managing the playback, stopping, and resetting of the animation.
 */
public class Animation {

    private int frameCount;
    private int frameDelay;
    private int currentFrame;
    private final int totalFrames;

    private boolean stopped;

    private boolean playOnce = false;
    private boolean hasEnded = false;

    private final List<Frame> frames = new ArrayList<>();

    /**
     * Constructor to create an animation from an array of images.
     * @param frames The array of BufferedImage for the frames.
     * @param frameDelay The delay between each frame in ticks.
     */
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

    /**
     * Starts the animation if it is not already running.
     */
    public void start() {
        if (!stopped) {
            return;
        }

        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
    }

    /**
     * Stops the animation.
     */
    public void stop() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = true;
    }

    /**
     * Restarts the animation from the beginning.
     */
    public void restart() {
        if (frames.isEmpty()) {
            return;
        }

        stopped = false;
        currentFrame = 0;
        hasEnded = false;
    }

    /**
     * Resets the animation to its initial state.
     */
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

    /**
     * Returns the current image of the animation.
     * @return The BufferedImage of the current frame.
     */
    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    /**
     * Updates the animation by advancing to the next frame if necessary.
     */
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

    /**
     * Sets whether the animation should play only once.
     * @param playOnce true if the animation should stop after one complete play.
     */
    public void setPlayOnce(boolean playOnce) {
        this.playOnce = playOnce;
    }

    /**
     * Checks if the animation has ended its playback (only if playOnce is enabled).
     * @return true if the animation has ended.
     */
    public boolean hasEnded() {
        return hasEnded;
    }

}