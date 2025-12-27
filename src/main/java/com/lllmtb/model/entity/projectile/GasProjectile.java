package com.lllmtb.model.entity.projectile;


import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

import java.util.ArrayList;
import java.util.List;

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
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<GameCommand>();
        hitWorm(list, entity, damage);
        list.add(removeProjectileBody());
        return list;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
