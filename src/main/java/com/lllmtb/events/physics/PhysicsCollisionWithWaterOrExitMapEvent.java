package com.lllmtb.events.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsCollisionWithWatterOrExitMapEvent extends PhysicsCollisionEvent {

    public PhysicsCollisionWithWatterOrExitMapEvent(Entity movingBody) {
        super(movingBody);
    }
}
