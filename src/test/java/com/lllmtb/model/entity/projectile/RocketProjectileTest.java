package com.lllmtb.model.entity.projectile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.lllmtb.events.EventBus;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
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
        Team team = new Team("TeamTest", 1, new Position[]{pos},0);
        Worm worm = new Worm("WormTest", team) {};
        UseParams params = new UseParams(0.0, pos, worm, 0);
        
        RocketProjectile rocket = new RocketProjectile(params);

     
        assertEquals(CollisionType.TOUCH, rocket.getCollisionType());
        assertEquals(10, rocket.getExplosionRadius());
        assertEquals(15, rocket.getSuffleRadius());
        assertEquals(30, rocket.getExplosionDamage());
        assertEquals(100.0, rocket.getExplosionForce());
        assertEquals("BazookaExplosion", rocket.getExplosionTexture());
    }

    @Test
        void testOnImpact_ReturnsExplosionCommand() {
            Position pos = new Position(0, 0);
            Team team = new Team("TeamTest", 1, new Position[]{pos}, 0);
            Worm worm = mock(Worm.class);
            when(worm.getTeam()).thenReturn(team);

            UseParams params = new UseParams(0.0, pos, worm, 0);
            RocketProjectile rocket = new RocketProjectile(params);

            GameWorld worldMock = mock(GameWorld.class);
            PhysicsEngine physicsMock = mock(PhysicsEngine.class);
            when(worldMock.getPhysics()).thenReturn(physicsMock);
            EventBus busMock = mock(EventBus.class);
            when(worldMock.getBus()).thenReturn(busMock);

            List<GameCommand> commands = rocket.onImpact(null);
            assertNotNull(commands);
            assertFalse(commands.isEmpty());

            for (GameCommand cmd : commands) {
                cmd.execute(worldMock);
            }

            verify(physicsMock).createExplosion(eq(rocket), eq(10), eq(15), eq(30), eq(100.0));
        }
}