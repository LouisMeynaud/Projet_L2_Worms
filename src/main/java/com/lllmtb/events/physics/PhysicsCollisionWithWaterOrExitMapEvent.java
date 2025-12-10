package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;

public record PhysicsCollisionWithWaterOrExitMapEvent(Entity entity) implements Event {
}
