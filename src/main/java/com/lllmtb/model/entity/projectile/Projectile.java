package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public abstract class Projectile extends Entity {
    private final PhysicsBody body;
    private final Worm owner;
    private final int damage = 0;

    /**
     * Create a new Projectile
     * @param params the {@code FireParams} sent to the Team
     */
    public Projectile(@NonNull UseParams params, double force, double mass, double height, double width) {
        Objects.requireNonNull(params);
        Vector velocity = new Vector(Math.cos(params.angleRadians), Math.sin(params.angleRadians)).scale(force);
        this.body = new PhysicsBody(this, params.pos, velocity, mass, height, width);
        this.owner = params.owner;
    }

    @Override
    public PhysicsBody getBody() {
        return body;
    }

    public Worm getOwner() {
        return owner;
    }

    /**
     * Called by collision detection when an impact is detected.
     */
    public abstract List<GameCommand> onImpact(Entity entity);

    protected void hitWorm(List<GameCommand> list, Entity entity, int damage){
        list.add(world -> world.getPhysics().remove(this.getBody()));
        if (entity instanceof Worm worm){
            worm.damageWorm(damage);
            if(!worm.isAlive()){
                list.add(world -> world.getPhysics().remove(worm.getBody()));
            }
        }
    }
}