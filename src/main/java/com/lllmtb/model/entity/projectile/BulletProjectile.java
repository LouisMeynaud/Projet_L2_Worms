package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

public final class BulletProjectile extends Projectile {
    private static final double force  = 100.0;
    private static final double mass   = 0.003;
    private static final double height = 1;
    private static final double width  = 1;

    public BulletProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public void onImpact(PhysicsEngine engine) {
        // trigger the reaction chain (explosion, events...)
    }
}
