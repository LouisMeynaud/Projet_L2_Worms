package com.lllmtb.model.entity.projectile;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public final class ElectronProjectile extends Projectile {
    private static final double force  = 20;
    private static final double mass   = 0;
    private static final double height = 2;
    private static final double width  = 2;
    private static final int damage = 10;

    private int bounces = 5;

    /**
     * Constructor for ElectronProjectile.
     * @param params UseParams containing initial parameters for the projectile.
     */
    public ElectronProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

    /**
     * Handles the impact of the projectile with an entity.
     * @param entity The entity that the projectile has impacted.
     * @return A list of GameCommands resulting from the impact.
     */
    @Override
    public List<GameCommand> onImpact(Entity entity) {
        if (entity == null) {
            return new ArrayList<>();
        }
        List<GameCommand> list = new ArrayList<GameCommand>();
        handleHit(list, entity, damage);
        Worm hitWorm = null;
        if (entity instanceof Worm) {
            hitWorm = (Worm) entity;
        }
        list.add(specialAbility(hitWorm));
        this.bounces--;
        if (this.bounces <= 0) {
            list.add(delete());
        }
        return list;
    }

    /**
     * Special ability to track the closest enemy worm.
     * @param wormToExclude The worm to exclude from targeting (usually the owner).
     * @return A GameCommand that adjusts the projectile's trajectory.
     */
    public GameCommand specialAbility(Worm wormToExclude) {
        Position myPos = this.getBody().getPos();
        Worm owner = this.getOwner();
        GameCommand trackWorm = (world -> {
            Worm target = world.getPhysics().findClosestEnemyFromPosition(myPos, owner.getTeam(), wormToExclude);
            if (target != null) {
                double angleVersEnnemi = PhysicsEngine.calculateAngleToBody(myPos, target.getBody());
                Vector velocity = new Vector(Math.cos(angleVersEnnemi), Math.sin(angleVersEnnemi)).scale(force);
                this.getBody().setVector(velocity);

            }
        });

        return trackWorm;
    }

    @Override
    public double getDamage() {
        return damage;
    }
}
