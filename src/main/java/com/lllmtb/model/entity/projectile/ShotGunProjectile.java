package com.lllmtb.model.entity.projectile;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

import java.util.List;

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
    public List<GameCommand> onImpact(Entity entity) {
        // trigger the reaction chain (explosion, events...)
        //TODO : Change value null
        return null;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
