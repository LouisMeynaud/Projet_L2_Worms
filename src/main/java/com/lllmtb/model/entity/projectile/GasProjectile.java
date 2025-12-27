package com.lllmtb.model.entity.projectile;


import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

public final class GasProjectile extends Projectile {
    private static final double force  = 5;
    private static final double mass   = 0;
    private static final double height = 10;
    private static final double width  = 10;
    private static final int damage = 3;

    public GasProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

    @Override
    public void onImpact(PhysicsEngine engine) {
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
