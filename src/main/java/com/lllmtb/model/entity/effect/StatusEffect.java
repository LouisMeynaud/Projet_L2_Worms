package com.lllmtb.model.entity.effect;

import com.lllmtb.model.entity.worm.Worm;

/**
 * Abstract class representing a status effect that can be applied to a worm.
 * Status effects have a duration and can apply effects each turn.
 */
public abstract class StatusEffect {

    /**
     * The damage dealt per turn
     */
    private final double damagePerTurn;

    /**
     * The remaining duration of the effect (in turns)
     */
    protected int remainingTurns;

    /**
     * The name of the effect
     */
    protected String name;

    /**
     * Constructor for a status effect
     * @param damagePerTurn the name of the effect
     * @param durationInTurns the duration of the effect in turns
     */
    public StatusEffect(double damagePerTurn, int durationInTurns, String name) {
        this.damagePerTurn = damagePerTurn;
        this.remainingTurns = durationInTurns;
        this.name = name;
    }

    /**
     * Gets the name of the effect
     * @return the name of the effect
     */
    public double getDamagePerTurn() {
        return this.damagePerTurn;
    }

    /**
     * Gets the remaining duration of the effect
     * @return the remaining turns
     */
    public int getRemainingTurns() {
        return this.remainingTurns;
    }

    /**
     * Gets the name of the effect
     * @return the name of the effect
     */
    public String getName() {
        return name;
    }

    /**
     * Decreases the remaining duration by 1
     */
    public void decreaseTurn() {
        this.remainingTurns--;
    }

    /**
     * Checks if the effect is still active
     * @return true if the effect has remaining turns, false otherwise
     */
    public boolean isActive() {
        return this.remainingTurns > 0;
    }

    /**
     * Applies the effect to a worm at the end of a turn
     * @param worm the worm to apply the effect to
     */
    public void applyEffect(Worm worm) {
        worm.damageWorm(this.damagePerTurn);
        this.decreaseTurn();
    }
}

