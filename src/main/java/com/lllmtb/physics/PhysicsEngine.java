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

    /** Whether wind simulation is enabled. */
    private boolean hasWind;

    /** The current wind vector applied to all bodies when {@code hasWind} is true. */
    private Vector wind;

    /** The duration of one simulation tick (in seconds). */
    public static final double ticktime = 0.0167; // Approx. 60 updates per second

    /** Internal counter used to regulate wind changes. */
    private int cpt = 0;


    /**
     * Constructs a new {@code PhysicsEngine} instance.
     * <p>
     * Gravity is initialized to (0, -9.81), simulating Earth's gravitational acceleration
     * in meters per second squared. Wind parameters are currently disabled.
     * </p>
     *
     * @param hasWind {@code true} if wind should be simulated; currently unused.
     */
    public PhysicsEngine(boolean hasWind) {
        this.bodies = new ArrayList<>();
        this.gravity = new Vector(0, -9.81);
        this.hasWind = hasWind;
        this.wind = new Vector(0, 0);
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

    /**
     * Returns whether wind simulation is currently enabled in the physics engine.
     *
     * @return {@code true} if wind effects are applied to all bodies during updates;
     *         {@code false} otherwise.
     */
    public boolean hasWind() {
        return hasWind;
    }

    /**
     * Returns the current wind vector applied to all bodies when wind simulation is enabled.
     * <p>
     * The wind vector represents the direction and intensity of the wind force currently
     * influencing the simulation. Its value may change over time if {@link #changeWind()} is called.
     * </p>
     *
     * @return the {@link Vector} representing the current wind force.
     */
    public Vector getWind() {
        return wind;
    }

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
     * Randomly changes the wind vector to simulate natural fluctuations in wind force and direction.
     * <p>
     * The change is small and based on random perturbations in both the x and y directions.
     * This method should typically be called less frequently than {@link #next()}, for example
     * once every few seconds of simulated time, to avoid erratic motion.
     * </p>
     * <p>
     * The resulting wind vector is added to each body’s acceleration during the next update.
     * </p>
     */
    public void changeWind(){
        if (this.hasWind) {
            Vector random = new Vector(Math.random() / 3 - 1/3, Math.random() / 3 - 1/3);
            this.wind.add(random);
        }
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
    private void nextStep() {
        for (PhysicsBody body : bodies) {
            Vector deltaV = body.getVector().copy();
            deltaV.scale(-0.2); // Simple air resistance
            deltaV.add(this.gravity);
            deltaV.add(this.wind);
            deltaV.scale(ticktime / body.getMass());

            Vector newVector = body.getVector().copy();
            newVector.add(deltaV);

            Position nextPos = newVector.newPos(body.getPos(), ticktime);
            body.setVector(newVector);

            if (!haveCollisionMap(body, nextPos)) {
                body.setPos(nextPos);
            }
        }
    }

    /**
     * Updates the simulation state by one tick.
     * <p>
     * This method increments the internal time counter, periodically updates the wind
     * vector (every 600 ticks if enabled), and performs one simulation step through
     * {@link #nextStep()}.
     * </p>
     */
    public void next(){
        cpt++;
        if(cpt >= 600){
            cpt = 0;
            changeWind();
        }
        nextStep();
    }
}