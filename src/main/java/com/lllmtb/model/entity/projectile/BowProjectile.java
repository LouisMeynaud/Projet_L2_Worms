package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class BowProjectile extends Projectile {
    private static final double force  = 60;
    private static final double mass   = 0.006;
    private static final double height = 2;
    private static final double width  = 2;
    private double damage = 10;


    /**
     * Constructor for BowProjectile.
     * @param params UseParams containing initial parameters for the projectile.
     */
    public BowProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 3) / 2, mass, height, width);
        this.damage = (int) (damage * Math.min(params.duration / 100, 3) / 2);

    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<>();
        handleHit(list, entity, damage);
        list.add(delete());
        return list;
    }

    @Override
    public double getDamage() {
        return damage;
    }
}
