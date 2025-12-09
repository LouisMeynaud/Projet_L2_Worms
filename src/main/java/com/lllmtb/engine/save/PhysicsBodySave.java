package com.lllmtb.engine.save;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsBody;

public class PhysicsBodySave {
    protected PhysicsBody physicsBody;
    protected int uuid;
    protected Entity entity;
    public PhysicsBodySave(PhysicsBody body) {
        physicsBody = body;
        uuid = physicsBody.getEntity().uuid;
        entity = physicsBody.getEntity();
    }
}
