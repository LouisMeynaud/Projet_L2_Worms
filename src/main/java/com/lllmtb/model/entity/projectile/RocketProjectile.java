package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

public final class RocketProjectile extends Projectile {
    private static final double force  = 50.0;
    private static final double mass   = 0.0045;
    private static final double height = 0.4;
    private static final double width  = 0.6;

    public RocketProjectile(@NonNull UseParams params) {
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