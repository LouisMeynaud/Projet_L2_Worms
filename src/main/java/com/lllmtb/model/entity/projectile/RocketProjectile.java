package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class RocketProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 50.0;
    private static final double mass   = 0.0045;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 40;

    private static final int RAYON_DEGAT_MAP = 10;
    private static final int RAYON_SOUFFLE = 15;
    private static final int DEGATS = 30;
    private static final double FORCE_SOUFFLE = 100;
    private static final String TEXTURE_KEY = "BazookaExplosion";

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
        handleHit(list, entity, damage);
        list.add((world -> {
            world.getPhysics().createExplosion(this, RAYON_DEGAT_MAP, RAYON_SOUFFLE, DEGATS, FORCE_SOUFFLE);
        }));
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
    public int getDamage() {
        return damage;
    }
}