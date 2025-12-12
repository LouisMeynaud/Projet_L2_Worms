package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class BulletProjectile extends Projectile {
    private static final double force  = 100.0;
    private static final double mass   = 0.003;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 20;

    public BulletProjectile(@NonNull UseParams params) {
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
    }
}
