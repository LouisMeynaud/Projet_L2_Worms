package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;

public record ProjectileRegisteredEvent(Projectile projectile, Entity source) implements Event {
}
