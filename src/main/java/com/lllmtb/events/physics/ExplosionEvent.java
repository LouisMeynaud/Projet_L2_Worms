package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;

public record ExplosionEvent(Position position, int radius, String animationKey) implements Event {
}