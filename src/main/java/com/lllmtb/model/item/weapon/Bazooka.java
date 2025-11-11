package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;

public final class Bazooka extends Weapon {

    @Override
    public WeaponId id() { return WeaponId.BAZOOKA; }

    @Override
    public Projectile createProjectile(FireParams params) {
        return new BulletProjectile(params);
    }
}