package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.item.weapon.FireParams;

public final class BulletProjectile extends Projectile {
    private final double force = 60.0;

    public BulletProjectile(FireParams params) {
        super(params);
    }

    @Override
    public void onImpact() {
        // trigger the reaction chain (explosion, events...)
    }
}