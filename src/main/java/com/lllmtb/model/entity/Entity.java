package com.lllmtb.model.entity;
import com.lllmtb.physics.PhysicsBody;

public abstract class Entity {

    // Champs
    private static int uuids = 0;
    public final int uuid;

    public Entity(){
        this.uuid = ++Entity.uuids;
    }

    public abstract PhysicsBody getBody();

    public abstract CollisionType getCollisionType();
}