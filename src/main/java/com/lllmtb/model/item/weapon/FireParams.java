package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;

/**
 * Simple immutable holder for firing parameters.
 */
public final class FireParams {
    public final double angleRadians;
    public final Position spawnPos;
    public final Worm owner;

    public FireParams(double angleRadians, Position spawnPos, Worm owner) {
        this.angleRadians = angleRadians;
        this.spawnPos = spawnPos;
        this.owner = owner;
    }
}