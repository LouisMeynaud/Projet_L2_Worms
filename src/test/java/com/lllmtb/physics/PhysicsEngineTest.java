package com.lllmtb.physics;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.worm.*;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {

//    private Map map;
//    private PhysicsEngine pE;
//    private Position p;
//    private Worm w;
//    private PhysicsBody pB;
//
//    @BeforeEach
//    void setUp() {
//        map = MapLoader.loadMatrix("mapTest");
//        pE = new PhysicsEngine(map, true);
//        p = new Position(4, 5);
//        w = new Worm("José", new Team("Team", 1, new Position[]{p}));
//        assertTrue(map.inMap(p));
//        pB = w.getBody();
//        pB.setVector(new Vector(-5, -10));
//        pE.add(pB);
//    }
//
//    @Test
//    void testGetWindInitial() {
//        Vector wind = pE.getWind();
//        assertNotNull(wind);
//        assertEquals(0, wind.getX());
//        assertEquals(0, wind.getY());
//    }
//
//    @Test
//    void testHasWind() {
//        assertTrue(pE.hasWind());
//    }
//
//    @Test
//    void testChangeWind_whenWind() {
//        Vector before = pE.getWind();
//        pE.changeWind();
//        Vector after = pE.getWind();
//        assertNotEquals(before.getX(), after.getX());
//        assertNotEquals(before.getY(), after.getY());
//    }
//
//    @Test
//    void testChangeWind_whenNoWind() {
//        PhysicsEngine tpE = new PhysicsEngine(map, false);
//        tpE.add(pB);
//
//        Vector before = tpE.getWind();
//        tpE.changeWind();
//        Vector after = tpE.getWind();
//        assertTrue(before.equals(after));
//    }
//
//    @Test
//    void testGravityGetter() {
//        Vector gravity = pE.getGravity();
//        assertEquals(0, gravity.getX());
//        assertEquals(-9.81, gravity.getY());
//    }
//
//    @Test
//    void testAddAndRemove() {
//        assertTrue(pE.getBodies().contains(pB));
//        pE.remove(pB);
//        assertFalse(pE.getBodies().contains(pB));
//    }
//
//    @Test
//    void testClear() {
//        pE.clear();
//        assertTrue(pE.getBodies().isEmpty());
//    }
//
//    @Test
//    void testNextStep(){
//        double ticktime = 0.0167;
//        Vector initialVector = pB.getVector();
//        Position initialPos = pB.getPos();
//        pE.nextStep(ticktime);
//        Position newPos = pB.getPos();
//        Vector newVector = pB.getVector();
//        assertEquals(initialPos.getX(), newPos.getX());
//        assertEquals(initialPos.getY(), newPos.getY());
//        assertNotEquals(initialVector.getX(), newVector.getX());
//        assertNotEquals(initialVector.getY(), newVector.getY());
//    }
//
//    @Test
//    void testNextStep_whenCollision() {
//        double ticktime = 0.0167;
//        PhysicsEngine tpE = new PhysicsEngine(map, false);
//        tpE.add(pB);
//        System.out.println(map);
//        for (int i = 0; i < 605; i++) {
//            // System.out.printf("\nPOS : %s, %s | inMap = %b", pB.getPos().getX(), pB.getPos().getY(), map.inMap(pB.getPos()));
//            tpE.nextStep(ticktime);
//        }
//    }
//
//    @Test
//    void testNextStep_whenProjectile(){
//        double ticktime = 0.0167;
//        Projectile pr = new BulletProjectile(new FireParams(0, p, w));
//        pE.add(pr.getBody());
//        pE.nextStep(ticktime);
//    }
//
//    @Test
//    void testWindChangesEvery600Ticks(){
//        double ticktime = 0.0167;
//        Vector windBefore = pE.getWind();
//        for (int i = 0; i < 605; i++) {
//            pE.next(ticktime);
//        }
//        Vector windAfter = pE.getWind();
//        assertNotEquals(windBefore.getX(), windAfter.getX());
//        assertNotEquals(windBefore.getY(), windAfter.getY());
//    }
//
//    @Test
//    void testNext(){
//        double ticktime = 0.0167;
//        pE.next(ticktime);
//        assertTrue(pB.getPos().getX() < 10);
//    }
}

