package com.lllmtb.model.entity.projectile;


import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

import java.util.List;

public final class ElectronProjectile extends Projectile {
    private static final double force  = 20;
    private static final double mass   = 0;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 20;

    public ElectronProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        Position myPos = this.getBody().getPos();
        Worm owner = this.getOwner();
        Worm target = engine.findClosestEnemyFromPosition(myPos, owner.getTeam());
        if (target != null) {
            Position targetPos = target.getBody().getPos();
            double dx = targetPos.getX() - myPos.getX();
            double dy = targetPos.getY() - myPos.getY();
            double angleVersEnnemi = Math.atan2(dy, dx);
            Vector velocity = new Vector(Math.cos(angleVersEnnemi), Math.sin(angleVersEnnemi)).scale(force);
            this.getBody().setVector(velocity);
        }
    }

    public GameCommand specialAbility(){

    }

    @Override
    public int getDamage() {
        return damage;
    }
}
