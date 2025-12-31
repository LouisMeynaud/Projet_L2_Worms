package com.lllmtb.model.entity;

import com.lllmtb.model.item.ItemId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public class Drop extends Entity {
    PhysicsBody body;
    public final ItemId itemIdToUnlock;

    public Drop(Position position, ItemId itemId) {
        this.itemIdToUnlock = itemId;
        this.body = new PhysicsBody(this, position, new Vector(0, 0), 0, 2, 2);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.CROSS;
    }

    @Override
    public PhysicsBody getBody() {
        return this.body;
    }
}

