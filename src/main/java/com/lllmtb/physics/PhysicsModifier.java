package com.lllmtb.physics;

import com.lllmtb.model.item.ItemId;

import org.eclipse.jdt.annotation.NonNull;
import java.util.Objects;

public class PhysicsModifier {
    public final Transform transform;
    public final ItemId source;
    public double remaining;
    public final boolean oneShot;
    public boolean enabled;

    public PhysicsModifier(@NonNull Transform transform, ItemId source, double duration, boolean enabled) {
        this.transform = Objects.requireNonNull(transform);
        this.source = source;
        this.remaining = duration;
        this.oneShot = false;
        this.enabled = enabled;
    }

    public PhysicsModifier(@NonNull Transform transform, ItemId source) {
        this.transform = Objects.requireNonNull(transform);
        this.source = source;
        this.oneShot = true;
        this.enabled = true;
    }
}
