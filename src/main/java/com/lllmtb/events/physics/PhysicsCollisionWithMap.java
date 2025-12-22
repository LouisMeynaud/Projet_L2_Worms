package com.lllmtb.events.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsCollisionWithMap extends PhysicsCollisionEvent {
    public PhysicsCollisionWithMap(Entity movingBody) {
        super(movingBody);
    }
}
