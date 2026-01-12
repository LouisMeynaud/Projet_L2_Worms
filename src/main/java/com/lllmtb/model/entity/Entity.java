package com.lllmtb.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.physics.PhysicsBody;

public abstract class Entity {


    // Thread-safe counter for generating unique entity IDs.
    private static final AtomicInteger uuidCounter = new AtomicInteger(0);

    /**
     * Indicates if the worm is currently in contact with the ground.
     */
    protected boolean isOnGround = false;
    public final int uuid;
    public Entity() {
        this.uuid = uuidCounter.incrementAndGet();
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