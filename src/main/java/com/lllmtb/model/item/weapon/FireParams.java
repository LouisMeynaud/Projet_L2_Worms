package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Simple immutable holder for firing parameters.
 */
public final class FireParams {
    public final double angleRadians;
    public final Position spawnPos;
    public final Worm owner;

    public FireParams(double angleRadians, @NonNull Position spawnPos, @NonNull Worm owner) {
        this.angleRadians = angleRadians;
        this.spawnPos = Objects.requireNonNull(spawnPos);
        this.owner = Objects.requireNonNull(owner);
    }
}