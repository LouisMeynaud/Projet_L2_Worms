package com.lllmtb.physics;

import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.terrain.map.Map;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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
    private EventBus bus;

    /** The list of all physics bodies currently managed by this engine. */
    private CopyOnWriteArrayList<PhysicsBody> bodies;

    private ConcurrentHashMap<PhysicsBody, ArrayList<PhysicsModifier>> modifiers;

    /** The {@link Collision} object of the game*/
    private final Collision collision;

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
     * @param bus the global {@code EventBus} instance created upstream.
     * @param hasWind if wind should be simulated.
     */
    public PhysicsEngine(Map map, EventBus bus, boolean hasWind) {
        this.bus = bus;
        this.bodies = new CopyOnWriteArrayList<>();
        this.modifiers = new ConcurrentHashMap<>();
        this.collision = new Collision(map, this.bodies);
        this.gravity = new Vector(0, -9.81);
        this.hasWind = hasWind;
        this.wind = new Vector(0, 0);
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

    public void addModifier(PhysicsBody body, PhysicsModifier modifier, int duration) {
        modifiers.computeIfAbsent(body, b -> new ArrayList<>()).add(modifier);
    }

    public void removeModifier(PhysicsBody body, PhysicsModifier modifier) {
        if (modifiers.containsKey(body)) modifiers.get(body).remove(modifier);
    }

    /**
     * Removes all physics bodies from the simulation.
     * <p>
     * After calling this method, the physics engine will be empty.
     * </p>
     */
    public void clear() {
        bodies.clear();
        modifiers.clear();
    }

    /**
     * Randomly changes the wind vector to simulate natural fluctuations in wind force and direction.
     * <p>
     * The change is small and based on random perturbations in both the x and y directions.
     * This method should typically be called less frequently than {@link #next(double)}, for example
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
     * It calls a different method in Collision according to the body.
     * </p>
     * @param dt the duration of the simulation tick (in seconds).
     */
    protected void nextStep(double dt) {
        for (PhysicsBody body : bodies) {
            Vector deltaV = body.getVector().scale(-0.2).add(this.gravity); // Simple air resistance + gravity effects
            // only apply wind effects to Projectiles
            if (body.getEntity() instanceof Projectile) deltaV.add(this.wind);
            deltaV.scale(dt / body.getMass());

            Vector newVector = body.getVector().add(deltaV);

            // apply modifiers for this tick only - we don't want to stack them every tick - not sure about this, for example gravity in the vector keeps increasing when the parachute is deployed and we don't want that
            if (modifiers.containsKey(body)) for (PhysicsModifier m : modifiers.get(body)) newVector = m.apply(newVector);
            body.setVector(newVector);

            Position nextPos = newVector.newPos(body.getPos(), dt);

            Collision.Impact impact = collision.move(body, nextPos);
            // if collision
            if (impact.collided) {
                // if body slide along cliffs
                if (body.getEntity().getCollisionType() == CollisionType.SLIDE) {
                    // if the collision is with the ground
                    collision.slide(body, impact, bus);
                    // if body bounce
                } else if (body.getEntity().getCollisionType() == CollisionType.BOUNCE) {
                    collision.bounce(body, impact, 0.5);
                    // if body stick to what it has hit
                } else if (body.getEntity().getCollisionType() == CollisionType.TOUCH) {
                    collision.touch(body, impact);
                    // if body cross all (map and entities)
                } else {
                    collision.cross(body, nextPos);
                }
            } else {
                body.setPos(nextPos);
            }
        }
    }

    /**
     * Updates the simulation state by one tick.
     * <p>
     * This method increments the internal time counter, periodically updates the wind
     * vector (every 600 ticks if enabled), and performs one simulation step through
     * {@link #nextStep(double)}.
     * </p>
     * @param ticktime the duration of the simulation tick (in seconds).
     */
    public void next(double ticktime){
        cpt++;
        if(cpt >= 600){
            cpt = 0;
            changeWind();
        }
        nextStep(ticktime/1000.0);
    }
}