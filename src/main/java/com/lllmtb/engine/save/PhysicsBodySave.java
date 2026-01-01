package com.lllmtb.engine.save;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsBody;

public class PhysicsBodySave {
    public PhysicsBody physicsBody;
    public int uuid;
    public Entity entity;
    public PhysicsBodySave(PhysicsBody body) {
        physicsBody = body;
        uuid = physicsBody.getEntity().uuid;
        entity = physicsBody.getEntity();
    }
    public PhysicsBodySave(){

    }
}
