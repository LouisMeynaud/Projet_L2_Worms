package com.lllmtb.model.entity.effect;

import com.lllmtb.model.entity.worm.Worm;

/**
 * Poison effect that deals damage to a worm at the end of each turn.
 * The poison lasts for a specified number of turns.
 */
public class PoisonEffect extends StatusEffect {

    /**
     * Constructor for a poison effect
     */
    public PoisonEffect() {
        super(10, 5);
    }
}

