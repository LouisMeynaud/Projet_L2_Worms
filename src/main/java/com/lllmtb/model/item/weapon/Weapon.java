package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.item.Item;

/**
 * Immutable descriptor + factory for creating a single projectile when fired.
 */
public abstract class Weapon implements Item {
    public abstract WeaponId id();
    // + metadata (sprites, audio...)

    /**
     * Create a projectile for this firing action.
     * Implementations must return a fresh Projectile instance.
     * If firing is not allowed for other reasons this should still return a projectile
     * or higher-level Inventory.fire() should have prevented the call.
     */
    public abstract Projectile createProjectile(FireParams params);
}