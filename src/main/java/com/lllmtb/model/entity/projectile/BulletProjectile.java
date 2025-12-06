package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.weapon.FireParams;
import org.eclipse.jdt.annotation.NonNull;

public final class BulletProjectile extends Projectile {
    private final double force  = 60;
    private final double mass   = 0.001;
    private final double width  = 1;
    private final double height = 1;

    public BulletProjectile(@NonNull FireParams params) {
        super(params, 100.0, 0.001, 1, 1);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public void onImpact() {
        // trigger the reaction chain (explosion, events...)
    }
}