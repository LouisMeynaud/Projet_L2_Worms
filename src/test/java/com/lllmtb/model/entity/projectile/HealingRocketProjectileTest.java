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

class HealingRocketProjectileTest {

    @Test
    void testValeursRocketProjectile() {
        Position pos = new Position(0, 0);
        Team team = new Team("TeamTest", 1, new Position[]{pos});
        Worm worm = new Worm("WormTest", team) {};
        UseParams params = new UseParams(0.0, pos, worm);
        HealingProjectile healing = new HealingProjectile(params);


        assertEquals(CollisionType.TOUCH, healing.getCollisionType());
        assertEquals(0, healing.getExplosionRadius());
        assertEquals(15, healing.getSuffleRadius());
        assertEquals(-20, healing.getExplosionDamage());
        assertEquals(0, healing.getExplosionForce());
        assertEquals("HealingBazookaExplosion", healing.getExplosionTexture());
    }

//      TODO : Remake the test with the new implementation
//    @Test
//    void testOnImpact_CallCreateExplosion() {
//        Position pos = new Position(0, 0);
//        Team team = new Team("TeamTest", 1, new Position[]{pos});
//        Worm worm = new Worm("WormTest", team) {};
//        UseParams params = new UseParams(0.0, pos, worm);
//        HealingProjectile healing = new HealingProjectile(params);
//        PhysicsEngine engineMock = mock(PhysicsEngine.class);
//
//        healing.onImpact(engineMock);
//        verify(engineMock).createExplosion(healing, 0, 15, -20, 0);//verifie qu'on appelle bien la methode
//    }
}