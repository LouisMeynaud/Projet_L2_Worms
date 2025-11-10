package com.lllmtb.physics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {

    private PhysicsEngine pE;
    private PhysicsBody pB;

    @BeforeEach
    void setUp() {
        pE = new PhysicsEngine(true);
        pB = new PhysicsBody(new Position(0, 0), new Vector(1, 1), 1,1,1);
        pE.add(pB);
    }

    @Test
    void testTicktimeGetter() {
        assertEquals(0.0167, PhysicsEngine.getTicktime());
    }

    @Test
    void testGravityGetter() {
        Vector gravity = pE.getGravity();
        assertEquals(0, gravity.getX());
        assertEquals(-9.81, gravity.getY());
    }

    @Test
    void testAddAndRemove() {
        assertTrue(pE.getBodies().contains(pB));
        pE.remove(pB);
        assertFalse(pE.getBodies().contains(pB));
    }

    @Test
    void testClear() {
        pE.clear();
        assertTrue(pE.getBodies().isEmpty());
    }

    @Test
    void testNext() {
        pE = new PhysicsEngine(false);
        pB = new PhysicsBody(new Position(0, 0), new Vector(0, 0), 1, 1, 1);
        pE.add(pB);
        pE.next();
        assertTrue(pB.getVector().getY() < 0);
    }


}

