package com.lllmtb.model.entity.projectile;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class ShotGunProjectile extends Projectile {
    private static final double force  = 80;
    private static final double mass   = 0.030;
    private static final double height = 0.5;
    private static final double width  = 0.5;
    private static final int damage = 7;

    /**
     * Constructor for ShotGunProjectile.
     * @param params UseParams containing initial parameters for the projectile.
     */
    public ShotGunProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
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
