package com.lllmtb.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.physics.PhysicsBody;

public abstract class Entity {

    // Champs
    private static int uuids = 0;
    public final int uuid;

    public Entity() {
        this.uuid = ++Entity.uuids;
    }

    public abstract PhysicsBody getBody();


    /**
     * @return the {@link CollisionType} associated with the entity.
     */
    public abstract CollisionType getCollisionType();


    public List<GameCommand> update(long tickTime) {
        return new ArrayList<>();
    }
}