package com.lllmtb.model.entity.projectile;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

public final class ShotGunProjectile extends Projectile {
    private static final double force  = 80;
    private static final double mass   = 0.030;
    private static final double height = 0.5;
    private static final double width  = 0.5;
    private static final int damage = 50;

    public ShotGunProjectile(@NonNull UseParams params) {
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

    @Override
    public int getDamage() {
        return damage;
    }
}
