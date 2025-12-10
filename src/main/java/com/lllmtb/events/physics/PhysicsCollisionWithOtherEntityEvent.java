package com.lllmtb.events.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsCollisionWithOtherEntityEvent extends PhysicsCollisionEvent {
    private final Entity hitBody;

    public PhysicsCollisionWithOtherEntityEvent(Entity movingBody, Entity hitBody) {
        super(movingBody);
        this.hitBody = hitBody;
    }

    public Entity getHitBody() {
        return hitBody;
    }
}