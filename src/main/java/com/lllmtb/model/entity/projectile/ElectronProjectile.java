package com.lllmtb.model.entity.projectile;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public final class ElectronProjectile extends Projectile {
    private static final double force  = 20;
    private static final double mass   = 0;
    private static final double height = 2;
    private static final double width  = 2;
    private static final int damage = 10;

    private int bounces = 5;

    public ElectronProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

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

    public GameCommand specialAbility(Worm wormToExclude) {
        Position myPos = this.getBody().getPos();
        Worm owner = this.getOwner();
        GameCommand trackWorm = (world -> {
            Worm target = world.getPhysics().findClosestEnemyFromPosition(myPos, owner.getTeam(), wormToExclude);
            if (target != null) {
                Position targetPos = target.getBody().getPos();
                double targetCenterX = targetPos.getX() + (target.getBody().getWidth() / 2.0);
                double targetCenterY = targetPos.getY() + (target.getBody().getHeight() / 2.0);
                double dx = targetCenterX - myPos.getX();
                double dy = targetCenterY - myPos.getY();
                double angleVersEnnemi = Math.atan2(dy, dx);
                Vector velocity = new Vector(Math.cos(angleVersEnnemi), Math.sin(angleVersEnnemi)).scale(force);
                this.getBody().setVector(velocity);

            }
        });

        return trackWorm;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
