package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents the bazooka-type weapon.
 * <p>
 * This weapon fires a rocket projectile with explosive effects, using
 * the provided firing parameters. It serves as the canonical implementation
 * for the {@link WeaponId#BAZOOKA} type.
 */
public final class BazookaWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.BAZOOKA;
    }

    @Override
    public List<GameCommand> fire(@NonNull FireParams params) {
        GameCommand cmd = new RegisterProjectileCommand(new RocketProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
