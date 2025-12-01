package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Immutable data holder for weapon firing parameters.
 * <p>
 * This class encapsulates all necessary information for firing a projectile:
 * the firing angle, the spawn position, and the worm (shooter) who initiated the shot.
 * <p>
 * Instances of this class are immutable and thread-safe.
 */
public final class FireParams {

    public final double angleRadians;
    public final Position spawnPos;
    public final Worm owner;

    /**
     * Creates a new {@code FireParams} instance.
     * @param angleRadians the firing angle, in radians
     * @param spawnPos     the initial spawn position of the projectile (must not be {@code null})
     * @param owner        the worm that initiated the firing action (must not be {@code null})
     * @throws  NullPointerException if {@code spawnPos} or {@code owner} is {@code null}
     */
    public FireParams(double angleRadians, @NonNull Position spawnPos, @NonNull Worm owner) {
        this.angleRadians = angleRadians;
        this.spawnPos = Objects.requireNonNull(spawnPos);
        this.owner = Objects.requireNonNull(owner);
    }
}