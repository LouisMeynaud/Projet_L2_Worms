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
    void testAddAndRemove() {
        assertTrue(pE.getSetb().contains(pB));
        pE.remove(pB);
        assertFalse(pE.getSetb().contains(pB));
    }

    @Test
    void testClear() {
        pE.clear();
        assertTrue(pE.getSetb().isEmpty());
    }

    @Test
    void testNextWithWind() {
        Position initialPos = new Position(pB.getPos().getX(),  pB.getPos().getY());
        Vector initialVec = new Vector(pB.getVector().getX(),  pB.getVector().getY());
        pE.next();
        assertNotEquals(initialPos.getY(), pB.getPos().getY());
        assertNotEquals(initialVec.getY(), pB.getVector().getY());
    }

    @Test
    void testNextWithoutWind() {
        pE = new PhysicsEngine(false);
        pB = new PhysicsBody(new Position(0, 0), new Vector(0, 0), 1, 1, 1);
        pE.add(pB);
        pE.next();
        assertTrue(pB.getVector().getY() < 0);
    }
}

