package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class RocketProjectile extends Projectile {
    private static final double force  = 50.0;
    private static final double mass   = 0.0045;
    private static final double height = 0.4;
    private static final double width  = 0.6;
    private static final int damage = 30;

    public RocketProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<GameCommand>();
        hitWorm(list, entity, damage);
        // trigger the reaction chain (explosion, events...)
        return list;
        // trigger the reaction chain (explosion, events...)
    }
}