package com.lllmtb.engine;

import com.lllmtb.events.game.GamePauseEvent;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class GameLoop implements Runnable {
    private final GameWorld world;
    private volatile boolean running = true;
    private final double ticktime = 16; // ~60Hz
    private boolean isPaused = false;

    /**
     * Creates a new {@code GameLoop} instance.
     * @param world the game world to be updated by this game loop
     * @throws NullPointerException if {@code world} is {@code null}
     */
    public GameLoop(@NonNull GameWorld world) {
        this.world = Objects.requireNonNull(world);

        this.world.getBus().subscribe(GamePauseEvent.class, this::onPauseEvent);
    }

    /**
     * Handles game pause events to update the paused state of the game loop.
     * @param event the {@link GamePauseEvent} containing the new paused state
     * @throws NullPointerException if {@code event} is {@code null}
     */
    private void onPauseEvent(@NonNull GamePauseEvent event) {
        this.isPaused = Objects.requireNonNull(event).isPaused;
    }


    /**
     * The main game loop method.
     * It processes events, updates the game world, and maintains a consistent tick rate.
     */
    @Override
    public void run() {
        long last = System.nanoTime();
        int acc = 0;
        while (running) {
            long now = System.nanoTime();
            long elapsedMs = (now - last) / 1_000_000;
            last = now;

            if (!isPaused) {
                System.out.print(""); // TODO : Replace by another line which will be always effective
                // System.out.println("[" + String.format("%4d", acc++) + "] New tick " + elapsedMs + "ms");
                // System.out.println("Entities in the game "
                //         + world.getPhysics().getBodies().size() + " : "
                //         + Arrays.toString(world.getPhysics().getBodies().stream().map(PhysicsBody::getEntity).toArray()));
                // System.out.println("Bodies : " + Arrays.toString(world.getPhysics().getBodies().toArray()));
            }

            // 1) process queued external events (eg. input)
            world.getBus().processQueued();

            if (!isPaused) {
                // 2) update physics + TurnManager
                world.update(ticktime);

                // 3) process events generated during physics (collisions, explosions...)
                world.getBus().processQueued();
            }

            // 4) sleep to maintain tick rate
            try {
                Thread.sleep(Math.max(0, (long) ticktime - (System.nanoTime() - now) / 1_000_000));
            } catch (InterruptedException ignored) {
                stop();
            }
        }
    }

    /**
     * Stops the game loop and clears the event bus and physics engine.
     */
    public void stop() {
        running = false;
        world.getBus().clear();
        world.getPhysics().clear();
    }
}