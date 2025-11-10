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
    void testWindGetter() {
        Vector wind = pE.getWind();
        assertNotNull(wind);
        assertEquals(0, wind.getX());
        assertEquals(0, wind.getY());
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
    void testIsWindGetter() {
        assertTrue(pE.isWind());
        PhysicsEngine pE2 = new PhysicsEngine(false);
        assertFalse(pE2.isWind());
    }

    @Test
    void testNextWithoutWind() {
        pE = new PhysicsEngine(false);
        pB = new PhysicsBody(new Position(0, 0), new Vector(0, 0), 1, 1, 1);
        pE.add(pB);
        pE.next();
        assertTrue(pB.getVector().getY() < 0);
    }

    @Test
    void testWindRandomBranch() {
        PhysicsEngine pE = new PhysicsEngine(true);
        Vector before = new Vector(pE.getWind().getX(), pE.getWind().getY());
        for (int i = 0; i < 20; i++){
            pE.next();
        }
        Vector after = pE.getWind();
        assertNotNull(after);
        assertEquals(before.getClass(), after.getClass());
    }
}

