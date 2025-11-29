package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.weapon.FireParams;
import org.eclipse.jdt.annotation.NonNull;

public final class BulletProjectile extends Projectile {
    private final double force  = 60.0;
    private final double mass   = 1;
    private final double width  = 0.2;
    private final double height = 0.1;

    public BulletProjectile(@NonNull FireParams params) {
        super(params, CollisionType.TOUCH);
    }

    @Override
    public void onImpact() {
        // trigger the reaction chain (explosion, events...)
    }
}