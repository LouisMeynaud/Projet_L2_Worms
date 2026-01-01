package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class CatConcreteProjectile extends Projectile implements ExplosiveProjectile {
    private static final double force  = 100.0;
    private static final double mass   = 0.01;
    private static final double height = 4;
    private static final double width  = 4;
    private static final int damage = 0;

    private static final int RAYON_DEGAT_MAP = 3;
    private static final int RAYON_SOUFFLE = 3;
    private static final int DEGATS = 0;
    private static final double FORCE_SOUFFLE = 0;
    private static final String TEXTURE_KEY = "MissileExplosion";

    /**
     * Constructor for CatConcreteProjectile.
     * @param params UseParams containing initial parameters for the projectile.
     */
    public CatConcreteProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 3) / 2, mass, height, width);
        }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.BOUNCE;
    }

    /**
     * Handles the impact of the projectile with an entity.
     * @param entity The entity that the projectile has impacted.
     * @return A list of GameCommands resulting from the impact.
     */
    @Override
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<>();
        handleHit(list, entity, damage);
        list.add((world -> world.getPhysics().createExplosion(this, RAYON_DEGAT_MAP, RAYON_SOUFFLE, DEGATS, FORCE_SOUFFLE)));
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

