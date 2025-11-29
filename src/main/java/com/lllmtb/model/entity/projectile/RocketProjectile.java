package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.weapon.FireParams;
import org.eclipse.jdt.annotation.NonNull;

public final class RocketProjectile extends Projectile {
    private final double force  = 30.0;
    private final double mass   = 10;
    private final double width  = 0.6;
    private final double height = 0.4;

    public RocketProjectile(@NonNull FireParams params) {
        super(params, CollisionType.TOUCH);
    }

    @Override
    public void onImpact() {
        // trigger the reaction chain (explosion, events...)
    }
}