package com.lllmtb.model.entity.projectile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;

class RocketProjectileTest {

    @Test
    void testValeursRocketProjectile() {
        Position pos = new Position(0, 0);
        Team team = new Team("TeamTest", 1, new Position[]{pos});
        Worm worm = new Worm("WormTest", team) {};
        UseParams params = new UseParams(0.0, pos, worm);
        
        RocketProjectile rocket = new RocketProjectile(params);

     
        assertEquals(CollisionType.TOUCH, rocket.getCollisionType());
        assertEquals(10, rocket.getExplosionRadius());
        assertEquals(15, rocket.getSuffleRadius());
        assertEquals(30, rocket.getExplosionDamage());
        assertEquals(100.0, rocket.getExplosionForce());
        assertEquals("BazookaExplosion", rocket.getExplosionTexture());
    }

//      TODO : Remake the test with the new implementation
//    @Test
//    void testOnImpact_CallCreateExplosion() {
//        Position pos = new Position(0, 0);
//        Team team = new Team("TeamTest", 1, new Position[]{pos});
//        Worm worm = new Worm("WormTest", team) {};
//        UseParams params = new UseParams(0.0, pos, worm);
//
//        RocketProjectile rocket = new RocketProjectile(params);
//        PhysicsEngine engineMock = mock(PhysicsEngine.class);
//
//        rocket.onImpact(engineMock);
//        verify(engineMock).createExplosion(rocket, 10, 15, 30, 100.0);//verifie qu'on appelle bien la methode
//    }
}