package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class MineProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 35.0;
    private static final double mass   = 0.01;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 60;

    private static final int RAYON_DEGAT_MAP = 7;
    private static final int RAYON_SOUFFLE = 12;
    private static final int DEGATS = 30;
    private static final double FORCE_SOUFFLE = 50;
    private static final String TEXTURE_KEY = "MineExplosion";

    public MineProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 3) / 2, mass, height, width);
        }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public boolean blocksTurnEnd() {
        return false;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        if (entity == null) return List.of();
        List<GameCommand> list = new ArrayList<>();
        handleHit(list, entity, damage);
        list.add((world -> world.getPhysics().createExplosion(this, RAYON_DEGAT_MAP, RAYON_SOUFFLE, DEGATS, FORCE_SOUFFLE, false)));
        list.add(delete());
        return list;
    }

    @Override
    public int getExplosionRadius() {
        return RAYON_DEGAT_MAP;
    }

    @Override
    public int getSuffleRadius() {
        return RAYON_SOUFFLE;
    }
    @Override
    public int getExplosionDamage() {
        return DEGATS;
    }

    @Override
    public double getExplosionForce() {
        return FORCE_SOUFFLE;
    }

    @Override
    public String getExplosionTexture() {
        return TEXTURE_KEY;
    }

    @Override
    public double getDamage() {
        return damage;
    }
}
