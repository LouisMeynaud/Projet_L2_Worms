package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class BowProjectile extends Projectile {
    private static final double force  = 30;
    private static final double mass   = 0.006;
    private static final double height = 2;
    private static final double width  = 2;
    private int damage = 10;

    public BowProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 5) / 2, mass, height, width);
        this.damage = (int) (damage * Math.min(params.duration / 100, 5) / 2);

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
