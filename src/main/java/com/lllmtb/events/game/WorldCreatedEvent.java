package com.lllmtb.events.game;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;

public record WorldCreatedEvent(GameWorld world) implements Event {
}
