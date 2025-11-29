package com.lllmtb.physics;

import com.lllmtb.events.EventBus;
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

    private Map map;
    private PhysicsEngine pE;
    private Position pw;
    private Position pBP;
    private Worm w;
    private BulletProjectile bP;
    private PhysicsBody pBWorm;
    private PhysicsBody pBBP;
    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        map = MapLoader.loadMatrix("mapTest");
        eventBus = new EventBus(4);
        pE = new PhysicsEngine(map, true, eventBus);
        pw = new Position(4, 4);
        w = new Worm("José", new Team("Team", 1, new Position[]{pw}));
        assertTrue(map.inMap(pw));
        pBP = new Position(0, 5);
        bP = new BulletProjectile(new FireParams(0, pBP, w));
        assertTrue(map.inMap(pw));
        pBWorm = w.getBody();
        pBBP = bP.getBody();
        pBWorm.setVector(new Vector(-5, -10));
        pBWorm.setPos(pw);
        pBBP.setVector(new Vector(0, -4));
        pBBP.setPos(pBP);
        pE.add(pBWorm);
        pE.add(pBBP);
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
    void testChangeWind_whenWind() {
        Vector before = pE.getWind();
        pE.changeWind();
        Vector after = pE.getWind();
        assertNotEquals(before.getX(), after.getX());
        assertNotEquals(before.getY(), after.getY());
    }

    @Test
    void testChangeWind_whenNoWind() {
        PhysicsEngine tpE = new PhysicsEngine(map, false, eventBus);
        tpE.add(pBWorm);

        Vector before = tpE.getWind();
        tpE.changeWind();
        Vector after = tpE.getWind();
        assertTrue(before.equals(after));
    }

    @Test
    void testGravityGetter() {
        Vector gravity = pE.getGravity();
        assertEquals(0, gravity.getX());
        assertEquals(-9.81, gravity.getY());
    }

    @Test
    void testAddAndRemove() {
        assertTrue(pE.getBodies().contains(pBWorm));
        pE.remove(pBWorm);
        assertFalse(pE.getBodies().contains(pBWorm));
    }

    @Test
    void testClear() {
        pE.clear();
        assertTrue(pE.getBodies().isEmpty());
    }

    @Test
    void testNextStep(){
        double ticktime = 0.0167;
        Vector initialVector = pBWorm.getVector();
        Position initialPos = pBWorm.getPos();
        pE.nextStep(ticktime);
        Position newPos = pBWorm.getPos();
        Vector newVector = pBWorm.getVector();
        assertEquals(3.9332, newPos.getX());
        assertEquals(initialPos.getY(), newPos.getY());
        assertNotEquals(initialVector.getX(), newVector.getX());
        assertNotEquals(initialVector.getY(), newVector.getY());
    }

    @Test
    void testNextStep_whenCollision() {
        double ticktime = 0.0167;
        PhysicsEngine tpE = new PhysicsEngine(map, false, eventBus);
        tpE.add(pBWorm);
        tpE.add(pBBP);
        System.out.println(map);
        for (int i = 0; i < 605; i++) {
            // System.out.printf("\nPOS : %s, %s | inMap = %b", pB.getPos().getX(), pB.getPos().getY(), map.inMap(pB.getPos()));
            tpE.nextStep(ticktime);
        }
        assertEquals(0, pBBP.getPos().getX());
        assertEquals(4, pBBP.getPos().getY());
        assertEquals(3.7018629769216, pBWorm.getPos().getX());
        assertEquals(4, pBWorm.getPos().getY());
    }

    @Test
    void testWindChangesEvery600Ticks(){
        double ticktime = 0.0167;
        Vector windBefore = pE.getWind();
        for (int i = 0; i < 605; i++) {
            pE.next(ticktime);
        }
        Vector windAfter = pE.getWind();
        assertNotEquals(windBefore.getX(), windAfter.getX());
        assertNotEquals(windBefore.getY(), windAfter.getY());
    }

    @Test
    void testNext(){
        double ticktime = 0.0167;
        pE.next(ticktime);
        assertTrue(pBWorm.getPos().getX() < 10);
    }
}

