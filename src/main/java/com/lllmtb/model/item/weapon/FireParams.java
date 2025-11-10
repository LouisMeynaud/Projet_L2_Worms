package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.Vector;

/**
 * Simple immutable holder for firing parameters.
 */
public final class FireParams {
    public final double angleRadians;
    public final double power;
    public final Vector spawnPos;
    public final Entity owner;

    public FireParams(double angleRadians, double power, Vector spawnPos, Entity owner) {
        this.angleRadians = angleRadians;
        this.power = power;
        this.spawnPos = spawnPos;
        this.owner = owner;
    }
}