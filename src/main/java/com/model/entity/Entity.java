package com.model.entity;
import com.lllmtb.physics.PhysicsBody;

public abstract class Entity {

    // Champs
    protected PhysicsBody body;
    private static int uuids = 0;
    public int uuid;

    public Entity(PhysicsBody body){
        this.body = body;
        this.uuid = ++Entity.uuids;
    }



}
