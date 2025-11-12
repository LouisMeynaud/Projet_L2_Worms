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
        pE.next();
        assertTrue(pB.getVector().getY() < 0);
    }


}

