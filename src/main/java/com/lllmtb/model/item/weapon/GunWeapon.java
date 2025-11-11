package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public final class GunWeapon extends Weapon {

    @Override
    public WeaponId id() { return WeaponId.GUN; }

    @Override
    public Projectile createProjectile(@NonNull FireParams params) {
        return new BulletProjectile(Objects.requireNonNull(params));
    }
}
