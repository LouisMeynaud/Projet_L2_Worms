package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.PhysicsBody;

public record PhysicsModifierAddedEvent(PhysicsBody body, PhysicsModifier modifier, int duration) implements Event {
}
