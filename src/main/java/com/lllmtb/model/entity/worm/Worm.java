package com.lllmtb.model.entity.worm;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Represents a single worm entity in the game.
 * <p>A worm belongs to a {@link Team}, has its own {@link PhysicsBody}, {@link Statistics},
 * and can carry a currently selected weapon ({@link WeaponId}). Each worm has a name,
 * health-related statistics, and a physical presence in the game world.</p>
 * <p>The {@code Worm} class extends {@link Entity} and manages the worm's physical state,
 * spawning position, current weapon, and alive status.</p>
 */
public class Worm extends Entity {

    /** The physical body of the worm used for physics simulation and position tracking. */
    private final PhysicsBody body;

    /** The team this worm belongs to. */
    private Team team;

    /** The name of the worm. */
    public final String name;

    /** The currently selected weapon for the worm. */
    private WeaponId selfItem = null;

    /** The statistics of the worm (ex health, speed, strength). */
    private final Statistics stats;

    /** Indicates if worm is alive. */
    private boolean alive = true;

    /** Worms have to slide when they have a collision with a wall. */
    private CollisionType collisionType = CollisionType.SLIDE;

    /**
     * Private constructor to initialize a worm with statistics, a team and a spawn point.
     * @param name the name of the worm
     * @param stats the {@link Statistics} associated with the worm
     * @param team the {@link Team} the worm belongs to
     * @param spawn the spawn position where the worm is placed
     */
    private Worm(String name, Statistics stats, Team team, @NonNull Position spawn) {
        super();
        Objects.requireNonNull(spawn);
        this.body = new PhysicsBody(this, spawn, new Vector(0, 0), 100, 1, 1);
        this.name = name;
        this.stats = stats;
        this.team = team;
        this.spawn(spawn);
    }

    /**
     * Public constructor to create a worm with default statistics.
     * <p>By default, a worm starts with 100 health and basic movement and damage stats.</p>
     * @param name the name of the worm
     * @param team the {@link Team} the worm belongs to
     * @param spawn the spawn position of the worm
     */
    public Worm(String name, Team team, Position spawn) {
        this(name, new Statistics(100, 1, 1), team, spawn);
    }

    /**
     * Returns the currently selected weapon of the worm.
     * @return the current {@link WeaponId}, or {@code null} if none is selected
     */
    public WeaponId getSelfItem() {
        return this.selfItem;
    }

    /**
     * Returns the worm's statistics (e.g., health, damage, speed).
     * @return the {@link Statistics} of the worm
     */
    public Statistics getStats() {
        return this.stats;
    }

    /**
     * Returns the team this worm belongs to.
     * @return the {@link Team} of the worm
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * Returns the physics body representing the worm's physical state.
     * @return the {@link PhysicsBody} associated with this worm
     */
    public PhysicsBody getBody() {
        return this.body;
    }

    /**
     * Returns the type of collision
     * @return the {@link CollisionType} associated with the entity
     */
    @Override
    public CollisionType getCollisionType() {
        return this.collisionType;
    }

    /**
     * Sets the currently selected weapon for this worm.
     * @param id the {@link WeaponId} to assign as the current weapon
     */
    public void setSelfItem(WeaponId id) {
        this.selfItem = id;
    }

    /**
     * Spawns the worm at the specified position.
     * <p>This sets the worm's {@link PhysicsBody} position in the game world.</p>
     * @param spawn the {@link Position} to place the worm
     */
    public void spawn(Position spawn) {
        this.body.setPos(spawn);
    }

    /**
     * Returns whether the worm is alive.
     * @return {@code true} if the worm is alive, {@code false} otherwise
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Updates the worm's alive status based on its {@link Statistics}.
     * <p>If the worm's health (or other relevant stat) indicates it is dead,
     * this method will mark the worm as no longer alive.</p>
     */
    public void setDeath() {
        if (!this.stats.isAlive()) {
            this.alive = false;
        }
    }

    @Override
    public String toString() {
        return "Worm[" + uuid + "]{ " + (alive ? "♥" : "☠") + " \"" + name + "\" in team \"" + team.name + "\" }";
    }
}
