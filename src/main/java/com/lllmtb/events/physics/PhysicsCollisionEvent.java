package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;

public abstract class PhysicsCollisionEvent implements Event {
    private final Entity movingBody;

    public PhysicsCollisionEvent(Entity movingBody) {
        this.movingBody = movingBody;
    }

    public Entity getMovingBody() {
        return movingBody;
    }
}