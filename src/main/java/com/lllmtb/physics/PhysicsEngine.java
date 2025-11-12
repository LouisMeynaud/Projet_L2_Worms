package com.lllmtb.physics;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The {@code PhysicsEngine} class manages and updates a collection of {@link PhysicsBody} objects
 * that represent the physical state of entities in a simulation.
 * <p>
 * It applies physical laws such as gravity, motion, and collisions to all bodies within the engine,
 * and can be updated periodically (each tick) to simulate the passage of time.
 * </p>
 */
public class PhysicsEngine {

    /** The list of all physics bodies currently managed by this engine. */
    private ArrayList<PhysicsBody> bodies;

    /** The gravity vector applied uniformly to all bodies. */
    private Vector gravity;

    // private boolean isWind;
    // private Vector wind;

    /** The duration of one simulation tick (in seconds). */
    public static double ticktime = 0.0167; // Approx. 60 updates per second


    /**
     * Constructs a new {@code PhysicsEngine} instance.
     * <p>
     * Gravity is initialized to (0, -9.81), simulating Earth's gravitational acceleration
     * in meters per second squared. Wind parameters are currently disabled.
     * </p>
     *
     * @param isWind {@code true} if wind should be simulated; currently unused.
     */
    public PhysicsEngine(boolean isWind) {
        this.bodies = new ArrayList<>();
        this.gravity = new Vector(0, -9.81);
        // this.isWind = isWind;
        // this.wind = new Vector(0, 0);
    }


    /**
     * Returns the list of all physics bodies currently managed by the physics engine.
     *
     * @return an {@link ArrayList} containing all active {@link PhysicsBody} objects.
     */
    public ArrayList<PhysicsBody> getBodies() {
        return bodies;
    }


    /**
     * Returns the gravity vector applied to all bodies in the simulation.
     *
     * @return the {@link Vector} representing gravity.
     */
    public Vector getGravity() {
        return gravity;
    }


    /**
     * Returns the duration of one simulation tick, in seconds.
     * <p>
     * This value determines how frequently the physics engine updates
     * the state of all bodies in the system.
     * </p>
     *
     * @return the tick duration as a {@code double}.
     */
    public static double getTicktime() {
        return ticktime;
    }


    /*public boolean isWind() {
        return isWind;
    }
    public Vector getWind() {
        return wind;
    }*/


    /**
     * Adds a physics body to the simulation.
     *
     * @param body the {@link PhysicsBody} to add.
     * @throws NullPointerException if {@code body} is {@code null}.
     */
    public void add(PhysicsBody body) {
        Objects.requireNonNull(body);
        bodies.add(body);
    }


    /**
     * Removes a specific physics body from the simulation.
     * <p>
     * If the body is not present in the engine, this method has no effect.
     * </p>
     *
     * @param body the {@link PhysicsBody} to remove.
     */
    public void remove(PhysicsBody body) {
        bodies.remove(body);
    }


    /**
     * Removes all physics bodies from the simulation.
     * <p>
     * After calling this method, the physics engine will be empty.
     * </p>
     */
    public void clear() {
        bodies.clear();
    }


    /**
     * Advances the simulation by one tick.
     * <p>
     * This method updates all {@link PhysicsBody} instances managed by the engine,
     * applying gravity, air resistance, and other forces. The new positions and velocities
     * of each body are computed based on the elapsed time defined by {@link #ticktime}.
     * </p>
     * <p>
     * If a body’s next position results in a collision, its position is not updated
     * until collision handling is performed.
     * </p>
     */
    public void next() {
        for (PhysicsBody body : bodies) {
            Vector acc = body.getVector().copy();
            acc.scale(-0.2); // Simple air resistance
            acc.add(this.gravity);

            /*
            if (this.isWind) {
                Vector random = new Vector(Math.random() / 3, Math.random() / 3);
                if (Math.random() < 0.5) this.wind.add(random);
                else this.wind.sub(random);
                sumForces.add(this.wind);
            }
            */

            acc.scale(ticktime / body.getMass());

            Vector newVector = body.getVector().copy();
            newVector.add(acc);

            Position nextPos = newVector.newPos(body.getPos(), ticktime);
            body.setVector(newVector);

            if (!Collision.haveCollisionMap(body, nextPos)) {
                body.setPos(nextPos);
            }
        }
    }
}