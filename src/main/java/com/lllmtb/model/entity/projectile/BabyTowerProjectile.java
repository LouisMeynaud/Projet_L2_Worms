package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class BabyTowerProjectile extends Projectile {
    private static final double force  = 20.0;
    private static final double mass   = 0.000;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 3;

    public BabyTowerProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<GameCommand>();
        handleHit(list, entity, damage);
        list.add(delete());
        return list;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
