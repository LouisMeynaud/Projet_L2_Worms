package com.lllmtb.engine;

import com.lllmtb.events.input.ProcessInputsEvent;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class GameLoop implements Runnable {
    private final GameWorld world;
    private volatile boolean running = true;
    private final double ticktime = 8; // ~60Hz
    private boolean isPaused = false;
    private long totalTicks = 0;
    private int speedTurn = 1;

    /**
     * Creates a new {@code GameLoop} instance.
     * @param world the game world to be updated by this game loop
     * @exception NullPointerException if {@code world} is {@code null}
     */
    public GameLoop(@NonNull GameWorld world) {
        this.world = Objects.requireNonNull(world);
    }

    /**
     * The main game loop method.
     * It processes events, updates the game world, and maintains a consistent tick rate.
     */
    @Override
    public void run() {
        long last = System.nanoTime();
        while (running) {
            long now = System.nanoTime();
            long elapsedMs = (now - last) / 1_000_000;
            last = now;

            if (!isPaused) {
                // System.out.println("[" + String.format("%4d", totalTicks) + "] New tick " + elapsedMs + "ms");
                // System.out.println("Entities in the game "
                //         + world.getPhysics().getBodies().size() + " : "
                //         + Arrays.toString(world.getPhysics().getBodies().stream().map(PhysicsBody::getEntity).toArray()));
                // System.out.println("Bodies : " + Arrays.toString(world.getPhysics().getBodies().toArray()));

                // 0) Signal input processing to allow UI components to send input events
                world.getBus().postQueued(new ProcessInputsEvent());

                // 1) process queued external events (eg. input)
                world.getBus().processQueued();

                // 2) update physics + TurnManager
                world.update(elapsedMs * speedTurn);

                // 3) process events generated during physics (collisions, explosions...)
                world.getBus().processQueued();

                // 4) increase totalTicks
                totalTicks++;
            }

            // 5) sleep to maintain tick rate
            try {
                Thread.sleep(Math.max(0, (long) ticktime - (System.nanoTime() - now) / 1_000_000));
            } catch (InterruptedException ignored) {
                stop();
            }
        }
    }

    /**
     * Pauses or resumes the game loop.
     * @param isPaused {@code true} to pause the game, {@code false} to resume
     */
    public void pause(boolean isPaused) {
        this.isPaused = isPaused;
    }

    /**
     * Stops the game loop and clears the event bus and physics engine.
     */
    public void stop() {
        running = false;
        world.getBus().clear();
        world.getPhysics().clear();
    }

    public long getTicks() {
        return this.totalTicks;
    }

    public void setSpeedTurn(int speedTurn) {
        this.speedTurn = speedTurn;
    }
}