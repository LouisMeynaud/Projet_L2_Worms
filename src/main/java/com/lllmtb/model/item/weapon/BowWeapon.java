package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.BowProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents the standard gun-type weapon.
 * <p>
 * This weapon fires a single bullet projectile using the provided firing parameters.
 * It serves as the canonical implementation for the {@link WeaponId#GUN} type.
 */
public final class BowWeapon extends Weapon {

    /**
     * Returns the unique identifier for this weapon type.
     * @return the {@link WeaponId#GUN} constant
     */
    @Override
    public WeaponId id() {
        return WeaponId.BOW;
    }

    /**
     * Creates a new {@link BulletProjectile} based on the given firing parameters.
     * @param params the firing parameters defining the projectile’s origin and direction (must not be {@code null})
     * @return a new {@link BulletProjectile} initialized with the given parameters
     * @throws NullPointerException if {@code params} is {@code null}
     */
    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterProjectileCommand(new BowProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
