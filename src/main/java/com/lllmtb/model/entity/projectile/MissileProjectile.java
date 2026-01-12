package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class MissileProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 100.0;
    private static final double mass   = 0.001;
    private static final double height = 4;
    private static final double width  = 4;
    private static final int damage = 5;

    private static final int RAYON_DEGAT_MAP = 15;
    private static final int RAYON_SOUFFLE = 20;
    private static final int DEGATS = 80;
    private static final double FORCE_SOUFFLE = 150;
    private static final String TEXTURE_KEY = "MissileExplosion";

    public MissileProjectile(@NonNull UseParams params) {
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
