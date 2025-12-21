package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;

public final class HealingProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 50.0;
    private static final double mass   = 0.0045;
    private static final double height = 0.4;
    private static final double width  = 0.6;
    private static final int damage = -20;


    private static final int RAYON_DEGAT_MAP = 0;
    private static final int RAYON_SOUFFLE = 15;
    private static final int DEGATS = -20;
    private static final double FORCE_SOUFFLE = 0;
    private static final String TEXTURE_KEY = "HealingBazookaExplosion";

    public HealingProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public void onImpact(PhysicsEngine engine) {
        engine.createExplosion(this, RAYON_DEGAT_MAP, RAYON_SOUFFLE, DEGATS, FORCE_SOUFFLE);
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