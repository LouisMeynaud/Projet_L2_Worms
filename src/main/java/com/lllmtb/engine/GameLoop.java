package com.lllmtb.engine;

import com.lllmtb.physics.PhysicsBody;

import java.util.Arrays;

public class GameLoop implements Runnable {
    private final GameWorld world;
    private volatile boolean running = true;
    private final long ticktime = 16; // ~60Hz

    public GameLoop(GameWorld world) {
        this.world = world;
    }

    @Override
    public void run() {
        long last = System.nanoTime();
        int acc = 0;
        while (running) {
            long now = System.nanoTime();
            long elapsedMs = (now - last) / 1_000_000;
            System.out.println("[" + String.format("%4d", acc++) + "] New tick " + elapsedMs + "ms");
            System.out.println("Entities in the game "
                    + world.getPhysics().getBodies().size() + " : "
                    + Arrays.toString(world.getPhysics().getBodies().stream().map(PhysicsBody::getEntity).toArray()));
            System.out.println("Bodies : " + Arrays.toString(world.getPhysics().getBodies().toArray()));
            last = now;

            // 1) process queued external events (eg. input)
            world.getBus().processQueued();

            // 2) update physics
            world.getPhysics().next(ticktime);

            // 3) process events generated during physics (collisions, explosions...)
            world.getBus().processQueued();

            // 4) sleep to maintain tick rate
            try {
                Thread.sleep(Math.max(0, ticktime - (System.nanoTime() - now)/1_000_000));
            } catch (InterruptedException ignored) {}
        }
    }

    public void stop() {
        running = false;
        world.getBus().clear();
        world.getPhysics().clear();
    }
}