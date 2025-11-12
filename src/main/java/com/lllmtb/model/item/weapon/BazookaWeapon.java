package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public final class BazookaWeapon extends Weapon {

    @Override
    public WeaponId id() { return WeaponId.BAZOOKA; }

    @Override
    public Projectile createProjectile(@NonNull FireParams params) {
        return new RocketProjectile(Objects.requireNonNull(params));
    }
}