package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public abstract class Projectile extends Entity {
    private final PhysicsBody body;
    private final Worm owner;

    /**
     * Create a new Projectile
     * @param params the {@code FireParams} sent to the Team
     */
    public Projectile(@NonNull FireParams params, double force, double mass, double height, double width) {
        Objects.requireNonNull(params);
        Vector velocity = new Vector(Math.cos(params.angleRadians), Math.sin(params.angleRadians));
        velocity = velocity.scale(force);

        this.body = new PhysicsBody(this, params.spawnPos, velocity, mass, height, width);
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
    abstract void onImpact();
}