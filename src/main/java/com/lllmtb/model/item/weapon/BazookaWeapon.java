package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Represents the bazooka-type weapon.
 * <p>
 * This weapon fires a rocket projectile with explosive effects, using
 * the provided firing parameters. It serves as the canonical implementation
 * for the {@link WeaponId#BAZOOKA} type.
 */
public final class BazookaWeapon extends Weapon {

    /**
     * Returns the unique identifier for this weapon type.
     *
     * @return the {@link WeaponId#BAZOOKA} constant
     */
    @Override
    public WeaponId id() {
        return WeaponId.BAZOOKA;
    }

    /**
     * Creates a new {@link RocketProjectile} based on the given firing parameters.
     *
     * @param params the firing parameters defining the projectile’s origin and direction (must not be {@code null})
     * @return a new {@link RocketProjectile} initialized with the given parameters
     * @throws NullPointerException if {@code params} is {@code null}
     */
    @Override
    public Projectile createProjectile(@NonNull FireParams params) {
        return new RocketProjectile(Objects.requireNonNull(params));
    }
}
