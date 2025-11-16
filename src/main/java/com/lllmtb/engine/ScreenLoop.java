package com.lllmtb.engine;

import com.lllmtb.events.EventBus;


public class ScreenLoop implements Runnable {

    private final EventBus eventBus; 
    private volatile boolean running = true;
    private final long tickMs = 16;


    public ScreenLoop(EventBus bus) {
        this.eventBus = bus;
    }

    @Override
    public void run() {
        while (running) {
            eventBus.processQueued();
            try {
                Thread.sleep(tickMs);
            } catch (InterruptedException ignored) {
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
        eventBus.clear();
    }
}