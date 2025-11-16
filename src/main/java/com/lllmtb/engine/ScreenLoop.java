package com.lllmtb.engine;


import java.util.Arrays;

import com.lllmtb.ui.GraphicsUi.WindowsUi;

public class ScreenLoop implements Runnable {
    private final WindowsUi windows;
    private volatile boolean running = true;
    private final long tickMs = 16; // ~60Hz

    public ScreenLoop(WindowsUi windowsUi) {
        this.windows = windowsUi;
    }

    @Override
    public void run() {
        long last = System.nanoTime();
        int acc = 0;
        while (running) {
            // 1) process queued external events (eg. input)
            windows.getBus().processQueued();

            // 4) sleep to maintain tick rate
        //    try {
        //         Thread.sleep(Math.max(0, tickMs - (System.nanoTime() - now)/1_000_000));
        //     } catch (InterruptedE xception ignored) {}
        }
    }

    public void stop() {
        running = false;
        windows.getBus().clear();
    }
}