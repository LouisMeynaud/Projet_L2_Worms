package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Represents the standard gun-type weapon.
 * <p>
 * This weapon fires a single bullet projectile using the provided firing parameters.
 * It serves as the canonical implementation for the {@link WeaponId#GUN} type.
 */
public final class GunWeapon extends Weapon {

    /**
     * Returns the unique identifier for this weapon type.
     *
     * @return the {@link WeaponId#GUN} constant
     */
    @Override
    public WeaponId id() {
        return WeaponId.GUN;
    }

    /**
     * Creates a new {@link BulletProjectile} based on the given firing parameters.
     *
     * @param params the firing parameters defining the projectile’s origin and direction (must not be {@code null})
     * @return a new {@link BulletProjectile} initialized with the given parameters
     * @throws NullPointerException if {@code params} is {@code null}
     */
    @Override
    public Projectile createProjectile(@NonNull FireParams params) {
        return new BulletProjectile(Objects.requireNonNull(params));
    }
}
