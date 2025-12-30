package com.lllmtb.model.entity.worm;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RemoveEntityCommand;
import com.lllmtb.engine.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.effect.StatusEffect;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

/**
 * Represents a single worm entity in the game.
 * <p>A worm belongs to a {@link Team} and has its own {@link PhysicsBody} and {@link Statistics}.</p>
 */
public class Worm extends Entity {

    /**
     * The physical body of the worm used for physics simulation and position tracking.
     */
    private final PhysicsBody body;

    /**
     * The team this worm belongs to.
     */
    private transient Team team;

    /**
     * The name of the worm.
     */
    public final String name;

    /**
     * The statistics of the worm (ex health, speed, strength).
     */
    private final Statistics stats;

    /**
     * Indicates if worm is alive.
     */
    private boolean alive = true;

    /**
     * Worms have to slide when they have a collision with a wall.
     */
    private final CollisionType collisionType = CollisionType.SLIDE;

    /**
     * Direction the worm is facing: 1 for right, -1 for left.
     */
    private int facingDirection = 1;

    /**
     * Indicates if the worm is currently in contact with the ground.
     */
    private boolean isOnGround = false;

    /**
     * List of active status effects on the worm.
     */
    private final List<StatusEffect> statusEffects = new ArrayList<>();

    /**
     * Private constructor to initialize a worm with statistics, a team, a spawn point and animation.
     * @param name  the name of the worm
     * @param stats the {@link Statistics} associated with the worm
     * @param team  the {@link Team} the worm belongs to
     */
    private Worm(@NonNull String name, @NonNull Statistics stats, @NonNull Team team) {
        super();
        this.body = new PhysicsBody(this, new Position(0, 0), new Vector(0, 0), 0.01, 2, 1);
        this.name = Objects.requireNonNull(name);
        this.stats = Objects.requireNonNull(stats);
        this.team = Objects.requireNonNull(team);
        this.team.addWorm(this);
    }

    /**
     * Public constructor to create a worm with default statistics.
     * <p>By default, a worm starts with 100 health and basic movement and damage stats.</p>
     * @param name the name of the worm
     * @param team the {@link Team} the worm belongs to
     */
    public Worm(@NonNull String name, @NonNull Team team) {
        this(name, new Statistics(Statistics.getMaxHp(), 1, 1), team);
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
     * Gets the direction the worm is facing.
     * @return 1 for right, -1 for left
     */
    public int getFacingDirection() {
        return this.facingDirection;
    }

    /**
     * Sets the direction the worm is facing.
     * @param direction 1 for right, -1 for left
     */
    public void setFacingDirection(int direction) {
        if (direction == 1 || direction == -1) {
            this.facingDirection = direction;
        }
    }

    /**
     * Updates the facing direction based on the worm's velocity.
     * If velocity x is negative, face left; if positive, face right.
     */
    public void updateFacingDirection() {
        double velocityX = this.body.getVector().getX();
        if (velocityX < 0) {
            setFacingDirection(-1);
        } else if (velocityX > 0) {
            setFacingDirection(1);
        }
    }

    /**
     * Returns whether the worm is currently in contact with the ground.
     * @return {@code true} if the worm is on the ground, {@code false} otherwise
     */
    public boolean isOnGround() {
        return this.isOnGround;
    }

    /**
     * Sets whether the worm is in contact with the ground.
     * @param onGround {@code true} if the worm is on the ground, {@code false} otherwise
     */
    public void setOnGround(boolean onGround) {
        this.isOnGround = onGround;
    }

    /**
     * Spawns the worm at the specified position.
     * <p>This sets the worm's {@link PhysicsBody} position in the game world.</p>
     * @param spawn the {@link Position} to place the worm
     */
    public void spawn(@NonNull Position spawn) {
        this.body.setPos(Objects.requireNonNull(spawn));
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
    private GameCommand setDeath() {
        if (!this.stats.isAlive()) {
            this.alive = false;
            Logger.logger.debug("Worm {} from the team {} is dead", this.name, this.team.name);
            return new RemoveEntityCommand(this);
        }
        return null;
    }

    public GameCommand damageWorm(int damage) {
        this.stats.damageTake(damage);
        return this.setDeath();
    }

    public static boolean isInTheSameTeam(Worm worm1, Worm worm2) {
        return (worm1.team.equals(worm2.team));
    }

    /**
     * Adds a status effect to the worm.
     * @param effect the {@link StatusEffect} to add
     */
    public GameCommand addStatusEffect(StatusEffect effect) {
        if (effect != null) {
            for (StatusEffect existingEffect : this.statusEffects) {
                if (existingEffect.getClass().equals(effect.getClass())) {
                    this.statusEffects.remove(existingEffect);
                    break;
                }
            }
            this.statusEffects.add(effect);
        }
        return null;
    }

    /**
     * Gets all active status effects on the worm.
     * @return a list of {@link StatusEffect} currently applied to the worm
     */
    public List<StatusEffect> getStatusEffects() {
        return this.statusEffects;
    }

    /**
     * Removes a status effect from the worm.
     * @param effect the {@link StatusEffect} to remove
     */
    public void removeStatusEffect(StatusEffect effect) {
        this.statusEffects.remove(effect);
    }

    /**
     * Applies all active status effects to the worm.
     * Inactive effects are automatically removed.
     * @return a list of GameCommands generated by the effects (e.g., RemoveEntityCommand if worm dies)
     */
    public List<GameCommand> applyStatusEffects() {
        List<GameCommand> commands = new ArrayList<>();
        List<StatusEffect> expiredEffects = new ArrayList<>();
        for (StatusEffect effect : this.statusEffects) {
            if (effect.isActive()) {
                effect.applyEffect(this);
                // Check again after application if the effect has expired
                if (!effect.isActive()) {
                    expiredEffects.add(effect);
                }
                // Check if worm died from the effect
                GameCommand deathCommand = this.setDeath();
                if (deathCommand != null) {
                    commands.add(deathCommand);
                }
            } else {
                expiredEffects.add(effect);
            }
        }
        // Remove expired effects
        for (StatusEffect effect : expiredEffects) {
            this.statusEffects.remove(effect);
        }
        return commands;
    }

    public static void setMaxHp(int maxHp) {
        Statistics.setMaxHp(maxHp);
    }

    @Override
    public String toString() {
        return "Worm[" + uuid + "]{ " + (alive ? "♥" : "☠") + " \"" + name + "\" in team \"" + team.name + "\" }";
    }
}
