package com.lllmtb.physics;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

class ExplosionTest {

    private Map map;
    private CopyOnWriteArrayList<PhysicsBody> bodies;

    @BeforeEach
    void setUp() {
        map = new Map(100, 100, "MapTest");
        
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                map.setTile(new Position(x, y), Tile.DIRT);
            }
        }
        bodies = new CopyOnWriteArrayList<>();
    }

    private Worm createWorm() {
        Position[] spawn = new Position[]{new Position(0,0)};
        Team team = new Team("TestTeam", 1, spawn, 0);
        return new Worm("TestWorm", team) {
        };
    }

    @Test
    void testEffetExplosionMap_TrouDansTerre() {
        Explosion.explosionMapEffect(new Position(10, 10), 1, map);
        assertEquals(Tile.AIR, map.getTile(new Position(10, 10)));
    }

    @Test
    void testEffetExplosionMap_CreusePasEau() {
        map.setTile(new Position(10, 10), Tile.WATER);
        Explosion.explosionMapEffect(new Position(10, 10), 1, map);
        assertEquals(Tile.WATER, map.getTile(new Position(10, 10)));
    }

    @Test
    void testEffetSouffleExplosion_AffecteEtatSolWorm() {
        Worm worm = createWorm();
        worm.setOnGround(true); 
        
        PhysicsBody body = new PhysicsBody(worm, new Position(5, 5), new Vector(0, 0), 1, 2, 1);
        bodies.add(body);
        Explosion.explosionSuffleEffect(new Position(0, 0), 20, 100, map, bodies);
        assertFalse(worm.isOnGround());
    }

    @Test
    void testEffetDegatsExplosion() {
        Worm worm = createWorm();
        double initialHp = worm.getStats().getHp();
        
        PhysicsBody body = new PhysicsBody(worm, new Position(0, 0), new Vector(0, 0), 1, 2, 1);
        bodies.add(body);
        
        Explosion.explosionDamageEffect(new Position(0, 0), 10, 50, bodies, false);
        assertEquals(initialHp - 50, worm.getStats().getHp());
    }

    @Test
    void testEffetDegatsExplosion_degatDistance() {
        Worm worm = createWorm();
        double initialHp = worm.getStats().getHp();
        
        PhysicsBody body = new PhysicsBody(worm, new Position(5, 0), new Vector(0, 0), 1, 2, 1);
        bodies.add(body);
        Explosion.explosionDamageEffect(new Position(0, 0), 10, 50, bodies, false);
        assertEquals(initialHp - 25, worm.getStats().getHp());
    }

    @Test
    void testEffetDegatsExplosion_PasDegatTropLoin() {
        Worm worm = createWorm();
        double initialHp = worm.getStats().getHp();
        
        PhysicsBody body = new PhysicsBody(worm, new Position(20, 20), new Vector(0, 0), 1, 2, 1);
        bodies.add(body);
        Explosion.explosionDamageEffect(new Position(0, 0), 10, 50, bodies, false);
        assertEquals(initialHp, worm.getStats().getHp());
    }

    @Test
    void checkIfWormDeathAfterExplosion(){
        Worm worm = createWorm();
        worm.damageWorm(90);
        assertTrue(worm.isAlive());

        PhysicsBody body = new PhysicsBody(worm, new Position(0, 0), new Vector(0, 0), 1, 2, 1);
        bodies.add(body);

        Explosion.explosionDamageEffect(new Position(0, 0), 10, 100, bodies, false);
        assertFalse(worm.isAlive());

    }
}