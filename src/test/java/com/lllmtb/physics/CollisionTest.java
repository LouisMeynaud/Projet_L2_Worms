package com.lllmtb.physics;


import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionTest {

    Map map;
    Team team;
    Worm worm1;
    PhysicsBody pBWorm1;
    Worm worm2;
    PhysicsBody pBWorm2;
    UseParams fP1;
    BulletProjectile bP1;
    PhysicsBody pBBP1;
    UseParams fP2;
    BulletProjectile bP2;
    PhysicsBody pBBP2;
    CopyOnWriteArrayList<PhysicsBody> bodies;
    Collision collision;

    @BeforeEach
    void setUp() {
        map = MapLoader.loadMap("MapTestCollision");
        Position posW1 = new Position(0, 14);
        Position posW2 = new Position(14, 14);
        Position[] positions = {posW1, posW2};
        team = new Team("Jeff team", 2, positions,0);
        worm1 = new Worm("Jeff", team);
        worm2 = new Worm("José", team);
        team.addWorm(worm1);
        team.addWorm(worm2);
        pBWorm1 = new PhysicsBody(worm1, posW1, new Vector(5, 0), 1, 2, 1);
        pBWorm2 = new PhysicsBody(worm2, posW2, new Vector(5, 0), 1, 2, 1);
        Position posBP1 = new Position(14, 18);
        fP1 = new UseParams(0, posBP1, worm1, 0);
        bP1 = new BulletProjectile(fP1);
        pBBP1 = new PhysicsBody(bP1, posBP1, new Vector(0,0), 1, 1, 1);
        Position posBP2 = new Position(4, 6);
        fP2 = new UseParams(0, posBP2, worm2, 0);
        bP2 = new BulletProjectile(fP2);
        pBBP2 = new PhysicsBody(bP2, posBP2, new Vector(1, 0), 1, 1, 1);
        bodies = new CopyOnWriteArrayList<>();
        bodies.add(pBWorm1);
        bodies.add(pBWorm2);
        bodies.add(pBBP1);
        bodies.add(pBBP2);
        collision = new Collision(map, bodies, new EventBus());
    }

    @Test
    void Collision_whenWormHasNotCollision() {
        Position posFinal = new Position(5, 14);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        assertNull(impact.hitBody);
        assertEquals(posFinal, impact.collisionPos);
        assertEquals(1.0, impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertFalse(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_whenWormHasCollisionWithMap() {
        pBWorm1.setVector(new Vector(28, 0));
        Position posFinal = new Position(28, 14);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        assertNull(impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(14, impact.collisionPos.getY());
        assertEquals(0.5, impact.ti);
        assertEquals(-1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_whenWormHasCollisionWithMap2() {
        pBWorm1.setVector(new Vector(28, 0));
        pBWorm1.setPos((new Position(0, 13)));
        Position posFinal = new Position(28, 13);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        assertNull(impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(13, impact.collisionPos.getY());
        assertEquals(0.5, impact.ti);
        assertEquals(-1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_whenWormCanNotMove() {
        Position posFinal = new Position(19, 14);
        Collision.Impact impact = collision.move(pBWorm2, posFinal);
        assertNull(impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(14, impact.collisionPos.getY());
        assertEquals(0.0, impact.ti);
        assertEquals(-1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_whenWormCanNotMove2() {
        Position posFinal = new Position(19, 13);
        pBWorm2.setPos(new Position(14, 13));
        Collision.Impact impact = collision.move(pBWorm2, posFinal);
        assertNull(impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(13, impact.collisionPos.getY());
        assertEquals(0.0, impact.ti);
        assertEquals(-1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_WormWithProjectile() {
        Position pos = new Position(14, 16);
        pBWorm1.setPos(pos);
        pBWorm2.setPos(new Position(14, 14));
        pBWorm2.setVector(new Vector(0, 4));
        Position posFinal = new Position(14, 18);
        Collision.Impact impact = collision.move(pBWorm2, posFinal);
        assertEquals(pBBP1, impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(16, impact.collisionPos.getY());
        assertEquals(0.5, impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(-1, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_ProjectileWithoutCollision() {
        Position posFinal = new Position(5, 14);
        Collision.Impact impact = collision.move(pBBP1, posFinal);
        assertNull(impact.hitBody);
        assertEquals(posFinal, impact.collisionPos);
        assertEquals(1.0, impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertFalse(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_ProjectileCanNotMove() {
        Position posFinal = new Position(10, 6);
        Collision.Impact impact = collision.move(pBBP2, posFinal);
        assertNull(impact.hitBody);
        assertEquals(4, impact.collisionPos.getX());
        assertEquals(6, impact.collisionPos.getY());
        assertEquals(0.0, impact.ti);
        assertEquals(-1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_ProjectileWithProjectile() {
        pBBP2.setPos(new Position(7, 13));
        pBBP2.setVector(new Vector(0, -6));
        pBBP1.setPos(new Position(7, 10));
        Position posFinal = new Position(7, 7);
        Collision.Impact impact = collision.move(pBBP2, posFinal);
        assertEquals(pBBP1, impact.hitBody);
        assertEquals(7, impact.collisionPos.getX());
        assertEquals(11, impact.collisionPos.getY());
        assertTrue(0.35 > impact.ti);
        assertTrue(0.3 < impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(1, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void Collision_ProjectileWithWormHead() {
        Position posFinal = new Position(14, 12);
        pBBP1.setVector(new Vector(0, -6));
        Collision.Impact impact = collision.move(pBBP1, posFinal);
        assertEquals(pBWorm2, impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(16, impact.collisionPos.getY());
        assertTrue(0.35 > impact.ti);
        assertTrue(0.3 < impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(1, impact.normal.getY());
        assertTrue(impact.collided);
        assertFalse(impact.die);
    }

    @Test
    void TestSweptCircleTile_whenEndPosOutOfMap() {
        pBWorm1.setVector(new Vector(-10, 5));
        pBWorm1.setPos(new Position(1, 14));
        Collision.Impact impact = collision.move(pBWorm1, new Position(-9, 19));
        assertNull(impact.hitBody);
        assertEquals(0.0, impact.collisionPos.getX());
        assertEquals(14.5, impact.collisionPos.getY());
        assertEquals(0.1, impact.ti);
        assertEquals(1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertTrue(impact.die);
    }

    @Test
    void TestSweptCircleTile_whenEndPosOutOfMap2() {
        pBWorm1.setPos(new Position(0, 14));
        pBWorm1.setVector(new Vector(-10, 0));
        Collision.Impact impact = collision.move(pBWorm1, new Position(-10, 14));
        assertNull(impact.hitBody);
        assertEquals(0.0, impact.collisionPos.getX());
        assertEquals(14, impact.collisionPos.getY());
        assertEquals(0.0, impact.ti);
        assertEquals(1, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
        assertTrue(impact.die);
    }

    @Test
    void calculateTileNormal() {
        pBWorm1.setPos(new Position(7, 7));
        pBWorm1.setVector(new Vector(9, 0));
        Collision.Impact impact = collision.move(pBWorm1, new Position(16, 7));
        assertNull(impact.hitBody);
        assertEquals(7, impact.collisionPos.getX());
        assertEquals(7, impact.collisionPos.getY());
        assertEquals(0.0, impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertTrue(impact.collided);
    }

    @Test
    void sweptPointCircle_whenAlreadyInCollision() {
        pBBP1.setVector(new Vector(0, 0));
        Position posFinal = new Position(14, 18);
        Collision.Impact impact = collision.move(pBBP1, posFinal);
        assertNull(impact.hitBody);
        assertEquals(14, impact.collisionPos.getX());
        assertEquals(18, impact.collisionPos.getY());
        assertEquals(1.0, impact.ti);
        assertEquals(0, impact.normal.getX());
        assertEquals(0, impact.normal.getY());
        assertFalse(impact.collided);
    }

    @Test
    void slide() {
        pBWorm1.setPos(new Position(7, 10));
        pBWorm1.setVector(new Vector(5, -2));
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, GameMode.NORMAL);
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", configMap, 2,GameMode.NORMAL, true,false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        Position posFinal = new Position(12, 8);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        collision.slide(pBWorm1, impact, 1, gw);
        assertEquals(12, pBWorm1.getPos().getX());
        assertEquals(10.002, pBWorm1.getPos().getY());
    }

    @Test
    void slideWithCollision() {
        pBWorm1.setPos(new Position(6, 10));
        pBWorm1.setVector(new Vector(5, -2));

        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, GameMode.NORMAL);
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", configMap, 2,GameMode.NORMAL, true,false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        Collision.Impact impact = new Collision.Impact(0.0, new Vector(5,-2),pBWorm1, new Vector(1,0), null, false);
        collision.slide(pBWorm1, impact, 1, gw);
        assertEquals(6.002003999992, pBWorm1.getPos().getX());
        assertEquals(10.001999996000013, pBWorm1.getPos().getY());
        assertTrue(worm1.isOnGround());
    }

    @Test
    void slideWithNoMovement() {
        pBWorm1.setPos(new Position(6, 10));
        pBWorm1.setVector(new Vector(0, 0));

        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, GameMode.NORMAL);
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", configMap, 2,GameMode.NORMAL, true,false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        Collision.Impact impact = new Collision.Impact(0.0, new Vector(5,-2),pBWorm1, new Vector(0,-1), null, false);
        collision.slide(pBWorm1, impact, 1, gw);
        assertEquals(6.0, pBWorm1.getPos().getX());
        assertEquals(10.0, pBWorm1.getPos().getY());
    }

    @Test
    void slideImpactNormalIs0() {
        pBWorm1.setPos(new Position(7, 10));
        Collision.Impact impact = new Collision.Impact(pBWorm1.getPos());
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, GameMode.NORMAL);
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", configMap, 2,GameMode.NORMAL, true,false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        collision.slide(pBWorm1, impact, 1, gw);
        assertFalse(worm1.isOnGround());
        assertEquals(0, pBWorm1.getVector().getX());
    }

    @Test
    void slideImpactNormalIs0NotWorm() {
        pBBP1.setPos(new Position(7, 10));
        Collision.Impact impact = new Collision.Impact(pBBP1.getPos());
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, GameMode.NORMAL);
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", configMap, 2,GameMode.NORMAL, true,false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        collision.slide(pBBP1, impact, 1, gw);
        assertEquals(0, pBBP1.getVector().getX());
    }

    @Test
    void bounce() {
        pBWorm1.setVector(new Vector(28, 0));
        Position posFinal = new Position(28, 14);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        collision.bounce(pBWorm1, impact, 0.5);
        assertEquals(13.998, pBWorm1.getPos().getX());
        assertEquals(14.0, pBWorm1.getPos().getY());
        assertEquals(-14, pBWorm1.getVector().getX());
        assertEquals(0, pBWorm1.getVector().getY());
    }

    @Test
    void touch() {
        pBWorm1.setVector(new Vector(28, 0));
        Position posFinal = new Position(28, 14);
        Collision.Impact impact = collision.move(pBWorm1, posFinal);
        collision.touch(pBWorm1, impact);
        assertEquals(13.998, pBWorm1.getPos().getX());
        assertEquals(14.0, pBWorm1.getPos().getY());
        assertEquals(0, pBWorm1.getVector().getX());
        assertEquals(0, pBWorm1.getVector().getY());
    }

    @Test
    void cross() {
        Position pos = new Position(5, 12);
        collision.cross(pBWorm1, pos);
        assertEquals(pos, pBWorm1.getPos());
    }

    @Test
    void testInTrapOrLava_WhenNull() {
        assertNull(collision.inTrapOrLava(pBWorm1));
    }

    @Test
    void testInTrapOrLava_WhenInLava1() {
        pBWorm1.setPos(new Position(27, 6));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenInLava2() {
        pBWorm1.setPos(new Position(27, 7));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenInLava3() {
        pBWorm1.setPos(new Position(26.5, 6));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenInLava4() {
        pBWorm1.setPos(new Position(26.5, 7));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenInLava5() {
        pBWorm1.setPos(new Position(27, 5.5));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenInLava6() {
        pBWorm1.setPos(new Position(26.5, 5.5));
        assertEquals("LAVA", collision.inTrapOrLava(pBWorm1).name());
    }

    @Test
    void testInTrapOrLava_WhenNull2() {
        pBWorm1.setPos(new Position(0.5, 5.5));
        assertNull(collision.inTrapOrLava(pBWorm1));
    }
}