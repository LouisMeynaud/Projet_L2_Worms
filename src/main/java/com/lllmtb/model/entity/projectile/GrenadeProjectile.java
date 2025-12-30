package com.lllmtb.model.entity.projectile;

import org.eclipse.jdt.annotation.NonNull;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;

import java.util.ArrayList;
import java.util.List;

public final class GrenadeProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 50.0;
    private static final double mass   = 0.0060;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage    = 60;

    private static final int RAYON_DEGAT_MAP = 7;
    private static final int RAYON_SOUFFLE   = 7;
    private static final int DEGATS          = 50;
    private static final double FORCE_SOUFFLE = 100;
    private static final String TEXTURE_KEY   = "BazookaExplosion";


    private long tempsEcoule = 0;
    private long remainingTime = 4000;

    public GrenadeProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 3) / 2, mass, height, width);
        }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.BOUNCE;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        return new ArrayList<>();
    }

    @Override
    public List<GameCommand> update(long tickTime) {
        List<GameCommand> cmds = new ArrayList<>();
        this.tempsEcoule += tickTime;
        if (this.tempsEcoule >= this.remainingTime) {
            cmds.add(world -> {
                world.getPhysics().createExplosion(this, RAYON_DEGAT_MAP, RAYON_SOUFFLE, DEGATS, FORCE_SOUFFLE);
            });
            cmds.add(this.delete());
            this.remainingTime = Long.MAX_VALUE;
        }
        return cmds;
    }

    @Override public int getExplosionRadius() {
        return RAYON_DEGAT_MAP;
    }
    @Override public int getSuffleRadius() {
        return RAYON_SOUFFLE;
    }
    @Override public int getExplosionDamage() {
        return DEGATS;
    }
    @Override public double getExplosionForce() {
        return FORCE_SOUFFLE;
    }
    @Override public String getExplosionTexture() {
        return TEXTURE_KEY;
    }
    @Override public int getDamage() {
        return damage;
    }
}