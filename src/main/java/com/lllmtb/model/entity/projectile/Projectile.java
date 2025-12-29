package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RemoveEntityCommand;
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
    private int damage = 0;

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

    public abstract int getDamage();

    /**
     * Handle the hit on an entity, applying damage if it's a Worm.
     * @param list the list of GameCommands to add to
     * @param entity the entity that was hit
     * @param damage the damage to apply
     */
    protected void handleHit(List<GameCommand> list, Entity entity, int damage) {
        if (entity instanceof Worm worm) {
            list.add(worm.damageWorm(damage));
        }
    }

    protected GameCommand delete() {
        return new RemoveEntityCommand(this);
    }
}