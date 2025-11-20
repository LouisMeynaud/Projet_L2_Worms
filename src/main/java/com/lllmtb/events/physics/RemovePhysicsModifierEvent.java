package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Modifier;
import com.lllmtb.physics.PhysicsBody;

public record RemovePhysicsModifierEvent(PhysicsBody body, Modifier modifier) implements Event {}
