package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;

public record EntityRegisteredEvent(Entity entity, Entity source) implements Event {
}
