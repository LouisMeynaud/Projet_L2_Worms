package com.lllmtb.physics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.game.CoinCollisionEvent;
import com.lllmtb.events.physics.ExplosionEvent;
import com.lllmtb.events.physics.PhysicsModifierAddedEvent;
import com.lllmtb.events.physics.PhysicsModifierRemovedEvent;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.effect.BurnEffect;
import com.lllmtb.model.entity.effect.PoisonEffect;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.ExplosiveProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

/**
 * The {@code PhysicsEngine} class manages and updates a collection of {@link PhysicsBody} objects
 * that represent the physical state of entities in a simulation.
 * <p>
 * It applies physical laws such as gravity, motion, and collisions to all bodies within the engine,
 * and can be updated periodically (each tick) to simulate the passage of time.
 * </p>
 */
public class PhysicsEngine {
    private final GameWorld world;

    /**
     * The list of all physics bodies currently managed by this engine.
     */
    private final CopyOnWriteArrayList<PhysicsBody> bodies;

    private final ConcurrentHashMap<PhysicsBody, ArrayList<PhysicsModifier>> modifiers;

    /**
     * The {@link Collision} object of the game.
     */
    private final Collision collision;

    /**
     * The gravity vector applied uniformly to all bodies.
     */
    private final Vector gravity;

    /**
     * Whether wind simulation is enabled.
     */
    private final boolean hasWind;

    /**
     * The current wind vector applied to all bodies when {@code hasWind} is true.
     */
    private Vector wind;

    /**
     * Internal counter used to regulate wind changes.
     */
    private int cpt = 0;

    private static final double MAX_WORM_SPEED = 8.0;
    private static final double GROUND_FRICTION = 0.90;
    private static final double AIR_RESISTANCE_X = 0.98;

    /**
     * Constructs a new {@code PhysicsEngine} instance.
     * <p>
     * Gravity is initialized to (0, -9.81), simulating Earth's gravitational acceleration
     * in meters per second squared.
     * </p>
     * @param map     the {@code Map} instance representing the environment for collision detection.
     * @param world   the global {@code GameWorld} instance created upstream.
     * @param hasWind if wind should be simulated.
     * @param gameMode the gameMode of the game.
     */
    public PhysicsEngine(Map map, GameWorld world, boolean hasWind, GameMode gameMode) {
        this.world = world;
        this.bodies = new CopyOnWriteArrayList<>();
        this.modifiers = new ConcurrentHashMap<>();
        this.collision = new Collision(map, this.bodies, world.getBus());
        this.gravity = gameMode.getGravity();
        this.hasWind = hasWind;
        this.wind = new Vector(0, 0);
    }

    /**
     * Returns the list of all physics bodies currently managed by the physics engine.
     * @return an {@link CopyOnWriteArrayList} containing all active {@link PhysicsBody} objects.
     */
    public CopyOnWriteArrayList<PhysicsBody> getBodies() {
        return bodies;
    }

    /**
     * Returns the gravity vector applied to all bodies in the simulation.
     * @return the {@link Vector} representing gravity.
     */
    public Vector getGravity() {
        return gravity;
    }

    /**
     * Returns whether wind simulation is currently enabled in the physics engine.
     * @return {@code true} if wind effects are applied to all bodies during updates;
     * {@code false} otherwise.
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
     * @return the {@link Vector} representing the current wind force.
     */
    public Vector getWind() {
        return wind;
    }

    public ConcurrentHashMap<PhysicsBody, ArrayList<PhysicsModifier>> getModifiers() {
        return modifiers;
    }

    /**
     * Adds a physics body to the simulation.
     * @param body the {@link PhysicsBody} to add.
     * @throws NullPointerException if {@code body} is {@code null}.
     */
    public void add(@NonNull PhysicsBody body) {
        bodies.add(Objects.requireNonNull(body));
    }

    /**
     * Removes a specific physics body from the simulation.
     * <p>
     * If the body is not present in the engine, this method has no effect.
     * </p>
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
    protected void changeWind() {
        if (this.hasWind) {
            Vector random = new Vector(Math.random() / 2 - 1f / 4f, Math.random() / 2 - 1f / 4f);
            this.wind = random.scale(10000);
        }
    }

    /**
     * Adds a physics modifier to a specific body.
     * <p>
     * The modifier will be applied to the body during the next simulation update.
     * </p>
     * @param body     the {@link PhysicsBody} to which the modifier will be added.
     * @param modifier the {@link PhysicsModifier} to add.
     * @exception NullPointerException if {@code body} or {@code modifier} is {@code null}.
     */
    public void addModifier(@NonNull PhysicsBody body, @NonNull PhysicsModifier modifier) {
        Objects.requireNonNull(body);
        Objects.requireNonNull(modifier);
        modifiers.computeIfAbsent(body, b -> new ArrayList<>()).add(modifier);
        world.getBus().postQueued(new PhysicsModifierAddedEvent(body, modifier));
    }

    /**
     * Removes a specific physics modifier from a body.
     * <p>
     * If the modifier is not present on the body, this method has no effect.
     * </p>
     * @param body     the {@link PhysicsBody} from which the modifier will be removed.
     * @param modifier the {@link PhysicsModifier} to remove.
     * @throws NullPointerException if {@code body} or {@code modifier} is {@code null}.
     */
    public void removeModifier(@NonNull PhysicsBody body, @NonNull PhysicsModifier modifier) {
        Objects.requireNonNull(body);
        Objects.requireNonNull(modifier);
        List<PhysicsModifier> list = modifiers.get(body);
        if (list == null) {
            return;
        }

        if (list.remove(modifier)) {
            world.getBus().postQueued(new PhysicsModifierRemovedEvent(body, modifier));
        }
        if (list.isEmpty()) {
            modifiers.remove(body);
        }
    }

    /**
     * Removes all physics modifiers from a body that originate from a specific source item.
     * <p>
     * This method iterates through all modifiers applied to the body and removes those
     * whose source matches the provided {@code source} item ID.
     * </p>
     * @param body   the {@link PhysicsBody} from which modifiers will be removed.
     * @param source the {@link ItemId} representing the source of the modifiers to remove.
     * @throws NullPointerException if {@code body} is {@code null}.
     */
    public void removeModifierFor(@NonNull PhysicsBody body, ItemId source) {
        Objects.requireNonNull(body);
        List<PhysicsModifier> list = modifiers.get(body);
        if (list == null) {
            return;
        }

        // (iterate over a copy to avoid concurrent modification when modifiers remove themselves)
        for (PhysicsModifier m : new ArrayList<>(list)) {
            if (Objects.equals(m.source, source)) {
                list.remove(m);
                world.getBus().postQueued(new PhysicsModifierRemovedEvent(body, m));
            }
        }
        if (list.isEmpty()) {
            modifiers.remove(body);
        }
    }

    /**
     * Applies all active physics modifiers to a given velocity vector for a specific body.
     * <p>
     * This method iterates through all modifiers associated with the body, applying their
     * transformations to the provided velocity vector. Modifiers that are one-shot or have
     * expired durations are removed after application.
     * </p>
     * @param body the {@link PhysicsBody} whose modifiers will be applied.
     * @param v    the original {@link Vector} representing the velocity to be modified.
     * @param dt   the time delta (in seconds) since the last update, used for duration calculations.
     * @return a new {@link Vector} representing the modified velocity after applying all active modifiers.
     * @throws NullPointerException if {@code body} or {@code v} is {@code null}.
     */
    public Vector applyModifiers(@NonNull PhysicsBody body, @NonNull Vector v, double dt) {
        Objects.requireNonNull(body);
        Objects.requireNonNull(v);
        List<PhysicsModifier> list = modifiers.get(body);
        if (list == null || list.isEmpty()) {
            return v;
        }

        // (iterate over a copy to avoid concurrent modification when modifiers remove themselves)
        for (PhysicsModifier m : new ArrayList<>(list)) {
            if (!m.enabled) {
                continue;
            }
            v = m.transform.apply(v);
            if (m.oneShot) {
                removeModifier(body, m);
            } else {
                if (Double.isInfinite(m.remaining)) {
                    continue;
                }
                m.remaining -= dt;
                if (m.remaining <= 0) {
                    removeModifier(body, m);
                }
            }
        }
        return v;
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
            Vector deltaV = this.gravity;
            deltaV = deltaV.add(body.getVector().scale(-0.5));

            if (body.getEntity() instanceof Projectile) deltaV = deltaV.add(this.wind);
            deltaV = deltaV.scale(dt * body.getMass());

            Vector newVector = body.getVector().add(deltaV);

            // apply modifiers for this tick only - we don't want to stack them every tick
            newVector = applyModifiers(body, newVector, dt);

            if (Math.abs(newVector.getX()) < 0.1) {
                newVector = new Vector(0, newVector.getY());
            }

            if (body.getEntity() instanceof Worm) {
                double velX = newVector.getX();
                if (Math.abs(velX) > MAX_WORM_SPEED) {
                    velX = Math.signum(velX) * MAX_WORM_SPEED;
                    newVector = new Vector(velX, newVector.getY());
                }
            }

            body.setVector(newVector);

            Position nextPos = newVector.newPos(body.getPos(), dt);

            // check for collision
            Collision.Impact impact = collision.move(body, nextPos);
            // if collision
            if (impact.collided) {

                switch (body.getEntity().getCollisionType()) {
                    // if body slides along cliffs (if the collision is with the ground)
                    case SLIDE -> collision.slide(body, impact, dt, world);
                    // if body bounces
                    case BOUNCE -> collision.bounce(body, impact, 0.5);

                    // if body sticks to what it has hit
                    case TOUCH -> collision.touch(body, impact);

                    // else body crosses all (map and entities)
                    default -> collision.cross(body, nextPos);
                }

                ////////// process impact //////////

                // if the body must die (out of map or touch water)
                if (impact.die) {
                    remove(body);
                    Logger.logger.debug("Entity out of map");
                    // if the body is a worm, kill it
                    if (body.getEntity() instanceof Worm worm) {
                        world.queueCommand(worm.damageWorm(worm.getStats().getHp()));
                        world.getTurnManager().endTurn();
                    }

                } else {
                    // if the collision is with the map
                    if (impact.hitBody == null) {
                        // if the body is a projectile, call its onImpact with null
                        if (body.getEntity() instanceof Projectile proj) {
                            world.queueCommands(proj.onImpact(null));
                        // if the body is a drop, set it on ground
                        } else if (body.getEntity() instanceof Drop drop) {
                            drop.setOnGround(true);
                        }

                    // if the collision is with another entity
                    } else {
                        // if the body is a projectile, call its onImpact with the hit entity
                        if (body.getEntity() instanceof Projectile proj) {
                            world.queueCommands(proj.onImpact(impact.hitBody.getEntity()));
                            // if the hit entity is a drop, call its onImpact with the projectile to destroy it
                            if (impact.hitBody.getEntity() instanceof Drop drop) {
                                world.queueCommand(drop.onImpact(proj));
                            }

                        // if the hit entity is a projectile, call its onImpact with the body entity
                        } else if (impact.hitBody.getEntity() instanceof Projectile proj) {
                            world.queueCommands(proj.onImpact(body.getEntity()));
                        }

                        // if the body is a drop, call its onImpact with the hit entity
                        if (body.getEntity() instanceof Drop drop) {
                            if (impact.hitBody.getEntity() instanceof Drop) {
                                drop.setOnGround(true);
                            }
                            world.queueCommand(drop.onImpact(impact.hitBody.getEntity()));
                        }

                        // if the body is a coin and the hit entity is a worm, post a CoinCollisionEvent and remove the coin to give it to the worm
                        if (body.getEntity() instanceof Coin coin) {
                            if (impact.hitBody.getEntity() instanceof Worm worm) {
                                world.getBus().postQueued(new CoinCollisionEvent(coin, worm.getTeam()));
                                remove(body);
                            }
                        }

                        // if the body is a worm and the hit entity is a drop, call the drop's onImpact with the worm to give it to the worm
                        if (body.getEntity() instanceof Worm worm) {
                            if (impact.hitBody.getEntity() instanceof Drop drop) {
                                world.queueCommand(drop.onImpact(worm));
                            }
                        }
                    }
                }

            // if no collision, update position normally
            } else {
                body.setPos(nextPos);
                // If the body is a worm, mark it as not on ground when it's in the air
                if (body.getEntity() instanceof Worm) {
                    ((Worm) body.getEntity()).setOnGround(false);
                }
            }

            // apply friction / air resistance to worms
            if (body.getEntity() instanceof Worm worm) {
                Vector currentVector = body.getVector();
                double newVelX = currentVector.getX();

                if (worm.isOnGround()) {
                    if (Math.abs(currentVector.getY()) < 0.5) {
                        double friction = GROUND_FRICTION;
                        Position checkPos = new Position(body.getPos().getX(), body.getPos().getY() - 2);
                        if (collision.getMap().inMap(checkPos)) {
                            Tile t = collision.getMap().getTile(checkPos);
                            friction = t.getFriction();
                        }
                        newVelX *= friction;
                    }
                } else {
                    newVelX *= AIR_RESISTANCE_X;
                }

                if (Math.abs(newVelX) < 0.1) {
                    newVelX = 0;
                }

                body.setVector(new Vector(newVelX, currentVector.getY()));
            }

            // check if the body is in lava or trap
            if (body.getEntity() instanceof Worm worm) {
                Tile tile = collision.inTrapOrLava(body);
                if (tile == Tile.LAVA) {
                    world.queueCommand(worm.damageWorm(Tile.LAVA.getDamage()));
                    world.queueCommand(worm.addStatusEffect(new BurnEffect()));
                }
                if (tile == Tile.TRAP) {
                    world.queueCommand(worm.damageWorm(Tile.TRAP.getDamage()));
                    world.queueCommand(worm.addStatusEffect(new PoisonEffect()));
                }
            }
        }
    }

    /**
     * Updates the simulation state by one tick.
     * <p>
     * This method increments the internal time counter, periodically updates the wind
     * vector (every 1250 ticks if enabled), and performs one simulation step through
     * {@link #nextStep(double)}.
     * </p>
     * @param ticktime the duration of the simulation tick (in milliseconds).
     */
    public void next(double ticktime) {
        cpt++;
        if (cpt >= 1250) {
            cpt = 0;
            changeWind();
        }
        nextStep(ticktime / 1000.0);
    }
    /**
     * Updates all physics bodies and processes their game commands.
     * <p>
     * This method iterates through all {@link PhysicsBody} instances managed by the engine,
     * calls their associated entities' update methods, and collects any resulting
     * {@link GameCommand} objects. All collected commands are then executed in the context
     * of the current {@link GameWorld}.
     * </p>
     * @param tickTime the duration of the simulation tick (in milliseconds).
     */
    public void update(double tickTime) {
        List<GameCommand> commands = new ArrayList<>();
        for (PhysicsBody body : bodies) {
            Entity entity = body.getEntity();
            List<GameCommand> commandsList = entity.update((long) tickTime);
            commands.addAll(commandsList);

        }
        for (GameCommand cmd : commands) {
            cmd.execute(this.world);
        }
    }

    /**
     * Creates an explosion at the location of a given projectile.
     * <p>
     * This method applies damage to entities within the blast radius,
     * modifies the terrain within the map damage radius, and applies
     * a blast effect to nearby bodies based on the blast force.
     * </p>
     * @param projectile       the {@link Projectile} that caused the explosion.
     * @param mapDamageRadius  the radius (in tiles) around the explosion center where the map will be damaged.
     * @param blastRadius      the radius (in units) around the explosion center where entities will be affected.
     * @param damage           the amount of damage to apply to entities within the blast radius.
     * @param blastForce       the force applied to bodies within the blast radius.
     * @param heal             true if the Explosion give life , false if she take damage
     */
    public void createExplosion(@NonNull Projectile projectile, int mapDamageRadius, int blastRadius, int damage, double blastForce, boolean heal) {
        Position position = projectile.getBody().getPos();
        List<GameCommand> death = Explosion.explosionDamageEffect(position, blastRadius, damage, bodies, heal);
        for (GameCommand command : death) {
            world.queueCommand(command);
        }
        Explosion.explosionMapEffect(position, mapDamageRadius, collision.getMap());
        Explosion.explosionSuffleEffect(position, blastRadius, blastForce, collision.getMap(), bodies);
        world.getBus().postQueued(new ExplosionEvent(position, blastRadius, ((ExplosiveProjectile) projectile).getExplosionTexture()));
        world.getBus().postQueued(new MapUpdateEvent());
    }
    /**
     * Finds the closest enemy worm to a given position, excluding a specified worm.
     * <p>
     * This method searches through all physics bodies managed by the engine,
     * identifies those that are worms belonging to enemy teams, and calculates
     * the distance from the specified position. The closest enemy worm is returned.
     * </p>
     * @param searchPos      the {@link Position} from which to search for the closest enemy worm.
     * @param ownerTeam      the team of the searching entity, used to identify enemies.
     * @param wormToExclude  the {@link Worm} instance to exclude from the search (typically the searching worm itself).
     * @return the closest enemy {@link Worm} instance, or {@code null} if no enemies are found.
     */
    public Worm findClosestEnemyFromPosition(Position searchPos, Object ownerTeam, Worm wormToExclude) {
        return findClosestEnemyFromPosition(searchPos, ownerTeam, wormToExclude, this.bodies);
    }
    /**
     * Finds the closest enemy worm to a given position from a specified list of bodies, excluding a specified worm.
     * <p>
     * This method searches through the provided list of physics bodies,
     * identifies those that are worms belonging to enemy teams, and calculates
     * the distance from the specified position. The closest enemy worm is returned.
     * </p>
     * @param searchPos      the {@link Position} from which to search for the closest enemy worm.
     * @param ownerTeam      the team of the searching entity, used to identify enemies.
     * @param wormToExclude  the {@link Worm} instance to exclude from the search (typically the searching worm itself).
     * @param bodiesList     the list of {@link PhysicsBody} instances to search through.
     * @return the closest enemy {@link Worm} instance, or {@code null} if no enemies are found.
     */
    public Worm findClosestEnemyFromPosition(Position searchPos, Object ownerTeam, Worm wormToExclude, List<PhysicsBody> bodiesList) {
    return bodiesList.stream()
        .filter(b -> b.getEntity() instanceof Worm)
        .map(b -> (Worm) b.getEntity())
        .filter(w -> !Objects.equals(w.getTeam(), ownerTeam))
        .filter(w -> w != wormToExclude)
        .min(Comparator.comparingDouble(targetWorm -> {
            Position p = targetWorm.getBody().getPos();
            double dx = searchPos.getX() - p.getX();
            double dy = searchPos.getY() - p.getY();
            return (dx * dx) + (dy * dy);
        }))
        .orElse(null);

    }

    public static GameCommand fallDamage(Worm worm) {
        int seuil = -50;
        GameCommand death = null;
        double speedy = worm.getBody().getVector().getY();
        if (speedy < seuil) {
            double damage = ((speedy - seuil) * -0.7);
            death = worm.damageWorm(damage);
            Logger.logger.info("{} has fallen and lost {} health points!", worm.name, damage);
        }
        return death;
    }

    /**
     * Calculates the angle in radians from a given position to the center of a target physics body.
     * @param fromPos the {@link Position} from which to calculate the angle.
     * @param targetBody the target {@link PhysicsBody} to which the angle is calculated.
     * @return the angle in radians from {@code fromPos} to the center of {@code targetBody}.
     */
    public static double calculateAngleToBody(Position fromPos, PhysicsBody targetBody) {
    Position targetPos = targetBody.getPos();
    double targetCenterX = targetPos.getX() + (targetBody.getWidth() / 2.0);
    double targetCenterY = targetPos.getY() + (targetBody.getHeight() / 2.0);
    double dx = targetCenterX - fromPos.getX();
    double dy = targetCenterY - fromPos.getY();
    return Math.atan2(dy, dx);
}
}