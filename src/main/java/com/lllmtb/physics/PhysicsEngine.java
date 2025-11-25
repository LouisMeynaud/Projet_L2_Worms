package com.lllmtb.physics;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.terrain.map.Map;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private CopyOnWriteArrayList<PhysicsBody> bodies;

    /** The gravity vector applied uniformly to all bodies. */
    private Vector gravity;

    /** Whether wind simulation is enabled. */
    private boolean hasWind;

    /** The current wind vector applied to all bodies when {@code hasWind} is true. */
    private Vector wind;

    /** Internal counter used to regulate wind changes. */
    private int cpt = 0;


    /**
     * Constructs a new {@code PhysicsEngine} instance.
     * <p>
     * Gravity is initialized to (0, -9.81), simulating Earth's gravitational acceleration
     * in meters per second squared.
     * </p>
     *
     * @param map the {@code Map} instance representing the environment for collision detection.
     * @param hasWind if wind should be simulated.
     */
    public PhysicsEngine(Map map, boolean hasWind) {
        this.bodies = new CopyOnWriteArrayList<>();
        this.gravity = new Vector(0, -9.81);
        this.hasWind = hasWind;
        this.wind = new Vector(0, 0);
        Collision.init(this.bodies, map);
    }

    /**
     * Returns the list of all physics bodies currently managed by the physics engine.
     *
     * @return an {@link CopyOnWriteArrayList} containing all active {@link PhysicsBody} objects.
     */
    public CopyOnWriteArrayList<PhysicsBody> getBodies() {
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
    protected void changeWind(){
        if (this.hasWind) {
            Vector random = new Vector(Math.random() / 3 - 1f/3f, Math.random() / 3 - 1f/3f);
            this.wind = this.wind.add(random);
        }
    }

    /**
     * Advances the simulation by one tick.
     * <p>
     * This method updates all {@link PhysicsBody} instances managed by the engine,
     * applying gravity, air resistance, and other forces. The new positions and velocities
     * of each body are computed based on the elapsed time defined by the ticktime.
     * </p>
     * <p>
     * If a body’s next position results in a collision, its position is not updated
     * until collision handling is performed.
     * </p>
     * @param ticktime the duration of the simulation tick (in seconds).
     */
    protected void nextStep(double ticktime) {
        for (PhysicsBody body : bodies) {
            Vector deltaV = body.getVector().scale(-0.2).add(this.gravity); // Simple air resistance + gravity effects
            // only apply wind effects to Projectiles
            if (body.getEntity() instanceof Projectile) deltaV.add(this.wind);
            deltaV.scale(ticktime / body.getMass());

            Vector newVector = body.getVector().add(deltaV);
            Position nextPos = newVector.newPos(body.getPos(), ticktime);
            body.setVector(newVector);

            if (!Collision.haveCollisionMap(body, nextPos)) {
                body.setPos(nextPos);
            }
            else  {
                body.setPos(body.getVector().newPos(body.getPos(), ticktime));
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
     * @param ticktime the duration of the simulation tick (in seconds).
     */
    public void next(double ticktime){
        cpt++;
        if(cpt >= 600){
            cpt = 0;
            changeWind();
        }
        nextStep(ticktime/1000f);
    }
}