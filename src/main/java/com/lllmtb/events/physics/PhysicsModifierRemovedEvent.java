package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Transform;
import com.lllmtb.physics.PhysicsBody;

public record PhysicsModifierRemovedEvent(PhysicsBody body, Transform modifier) implements Event {}
