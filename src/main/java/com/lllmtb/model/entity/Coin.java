package com.lllmtb.model.entity;

import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public class Coin extends Entity {
    private final PhysicsBody body;
    public final int value;

    /**
     *
     * @param position the spawn position
     * @param value the amount that the coin give a are contact
     */
    public Coin(Position position, int value) {
        this.value = value;
        this.body = new PhysicsBody(this, position, new Vector(0, 0), 0.01, 1.3, 1.3);
    }
    /**
     *
     * @return the value of the coin
     */
    public int getValue() {
        return value;
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public PhysicsBody getBody() {
        return this.body;
    }
}
