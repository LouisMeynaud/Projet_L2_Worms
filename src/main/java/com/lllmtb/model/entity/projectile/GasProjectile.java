package com.lllmtb.model.entity.projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.model.entity.effect.PoisonEffect;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

public final class GasProjectile extends Projectile {
    private static final double force  = 5;
    private static final double mass   = 0;
    private static final double height = 10;
    private static final double width  = 10;
    private static final int damage = 1;
    private static final double radius = 6.0;

    public GasProjectile(@NonNull UseParams params) {
        super(params, force, mass, height, width);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        List<GameCommand> list = new ArrayList<>();
        list.add(world -> {
            CopyOnWriteArrayList<PhysicsBody> bodies = world.getPhysics().getBodies();

            Position pos = this.getBody().getPos();
            double centerX = pos.getX() + (this.getBody().getWidth() / 2.0);
            double centerY = pos.getY() + (this.getBody().getHeight() / 2.0);

            for (PhysicsBody body : bodies) {
                if (body.getEntity() instanceof Worm worm) {
                    double wormCenterX = body.getPos().getX() + (body.getWidth() / 2.0);
                    double wormCenterY = body.getPos().getY() + (body.getHeight() / 2.0);

                    double dx = wormCenterX - centerX;
                    double dy = wormCenterY - centerY;
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance <= radius) {
                        list.add(worm.addStatusEffect(new PoisonEffect()));
                    }
                }
            }
        });
        return list;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
