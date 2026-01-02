package com.lllmtb.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

public class CoinTest {

    @Test

    void testValue() {
        Position position = new Position(10.0, 20.0);
        int expectedValue = 100;
        Coin coin = new Coin(position, expectedValue);
        assertEquals(expectedValue, coin.getValue());
        assertEquals(expectedValue, coin.value);
    }

    @Test
    void testGetBody() {
        Position position = new Position(5.0, 5.0);
        Coin coin = new Coin(position, 50);

        PhysicsBody body = coin.getBody();

        assertEquals(2, body.getWidth());
        assertEquals(2, body.getHeight());
        assertEquals(coin, body.getEntity());
    }

    @Test
    void testGetCollisionType() {
        Coin coin = new Coin(new Position(0, 0), 10);
        assertEquals(CollisionType.TOUCH, coin.getCollisionType());
    }
}
