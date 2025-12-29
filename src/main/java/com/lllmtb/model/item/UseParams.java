package com.lllmtb.model.item;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Immutable data holder for item use parameters.
 * <p>
 * This class encapsulates all necessary information for using an item, including:
 * the firing angle, the spawn position, and the worm (owner) who initiated the use action.
 * <p>
 * Instances of this class are immutable and thread-safe.
 */
public final class UseParams {

    public final double angleRadians;
    public final Position pos;
    public final Worm owner;
    public final double duration;

    /**
     * Creates a new {@code UseParams} instance for firing parameters.
     * @param angleRadians the firing angle, in radians
     * @param pos          the position of the item use action (must not be {@code null})
     * @param owner        the worm that initiated the firing action
     * @param duration     the duration of the user clic
     * @throws NullPointerException if {@code pos} or {@code owner} is {@code null}
     */
    public UseParams(double angleRadians, @NonNull Position pos, @NonNull Worm owner, double duration) {
        this.angleRadians = angleRadians;
        this.pos = Objects.requireNonNull(pos);
        this.owner = Objects.requireNonNull(owner);
        this.duration = duration;
    }

    /**
     * Creates a new simple {@code UseParams} instance for tools.
     * @param owner the worm that initiated the use action
     * @throws NullPointerException if {@code owner} is {@code null}
     */
    public UseParams(@NonNull Worm owner) {
        this.angleRadians = 0;
        this.pos = owner.getBody().getPos();
        this.owner = Objects.requireNonNull(owner);
        this.duration = 0;
    }
}