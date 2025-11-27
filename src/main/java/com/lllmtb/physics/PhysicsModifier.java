package com.lllmtb.physics;

import com.lllmtb.model.item.ItemId;

public class PhysicsModifier {
    public final Transform transform;
    public final ItemId source;
    public double remaining;
    public final boolean oneShot;
    public boolean enabled;

    public PhysicsModifier(Transform transform, ItemId source, double duration, boolean enabled) {
        this.transform = transform;
        this.source = source;
        this.remaining = duration;
        this.oneShot = false;
        this.enabled = enabled;
    }

    public PhysicsModifier(Transform transform, ItemId source) {
        this.transform = transform;
        this.source = source;
        this.oneShot = true;
        this.enabled = true;
    }
}
