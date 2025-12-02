package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.weapon.FireParams;
import org.eclipse.jdt.annotation.NonNull;

public final class RocketProjectile extends Projectile {
    private static final double force  = 30.0;
    private static final double mass   = 10;
    private static final double height = 0.4;
    private static final double width  = 0.6;

    public RocketProjectile(@NonNull FireParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public void onImpact() {
        // trigger the reaction chain (explosion, events...)
    }
}