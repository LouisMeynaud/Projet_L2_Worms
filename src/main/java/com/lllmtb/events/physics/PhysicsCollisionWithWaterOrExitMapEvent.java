package com.lllmtb.events.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsCollisionWithWaterOrExitMapEvent extends PhysicsCollisionEvent {

    public PhysicsCollisionWithWaterOrExitMapEvent(Entity movingBody) {
        super(movingBody);
    }
}
