package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;

public record GroundCheckCollision(Entity worm) implements Event {
}
