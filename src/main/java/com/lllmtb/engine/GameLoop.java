package com.lllmtb.engine;

public class GameLoop implements Runnable {
    private final GameWorld world;
    private volatile boolean running = true;
    private final long tickMs = 16; // ~60Hz

    public GameLoop(GameWorld world) {
        this.world = world;
    }

    @Override
    public void run() {
        long last = System.nanoTime();
        while (running) {
            long now = System.nanoTime();
            long elapsedMs = (now - last) / 1_000_000;
            System.out.println(elapsedMs);
            last = now;

            // 1) process queued external events (eg. input)
            world.getBus().processQueued();

            // 2) update physics
            world.getPhysics().next();  // will have to be adapted to take in (long elapsedMs) = dt which PhysicsBody can use instead of tickTime

            // 3) process events generated during physics (collisions, explosions...)
            world.getBus().processQueued();

            // 4) sleep to maintain tick rate
            try {
                Thread.sleep(Math.max(0, tickMs - (System.nanoTime() - now)/1_000_000));
            } catch (InterruptedException ignored) {}
        }
    }

    public void stop() {
        running = false;
    }
}