package com.lllmtb.physics;

import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.worm.*;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {

    private PhysicsEngine pE;
    private PhysicsBody pB;

    @BeforeEach
    void setUp() {
        Map map = MapLoader.loadMatrix("mapTest");
        pE = new PhysicsEngine(map, false);
        Worm ent = new Worm(new Position(1, 1));
        pB = ent.getBody();
        pE.add(pB);
    }

    @Test
    void testGetWindInitial() {
        Vector wind = pE.getWind();
        assertNotNull(wind);
        assertEquals(0, wind.getX());
        assertEquals(0, wind.getY());
    }

    @Test
    void testHasWind() {
        assertTrue(pE.hasWind());
    }

    @Test
    void testChangeWind() {
        Vector before = pE.getWind().copy();
        pE.changeWind();
        Vector after = pE.getWind();
        assertNotEquals(before.getX(), after.getX());
        assertNotEquals(before.getY(), after.getY());
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
    void testNextStep(){
        Vector initialVector = pB.getVector().copy();
        Position initialPos = pB.getPos();
        pE.nextStep();
        Position newPos = pB.getPos();
        Vector newVector = pB.getVector();
        assertNotEquals(initialPos.getX(), newPos.getX());
        assertNotEquals(initialPos.getY(), newPos.getY());
        assertNotEquals(initialVector.getX(), newVector.getX());
        assertNotEquals(initialVector.getY(), newVector.getY());
    }

    @Test
    void testNext(){
        Vector windBefore = pE.getWind().copy();
        for (int i = 0; i < 600; i++) {
            pE.next();
        }
        Vector windAfter = pE.getWind();
        assertNotEquals(windBefore.getX(), windAfter.getX());
        assertNotEquals(windBefore.getY(), windAfter.getY());
        assertTrue(pB.getPos().getY() < 0);
    }

}

