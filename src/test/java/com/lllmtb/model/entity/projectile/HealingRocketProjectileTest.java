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
    @Test
    void testOnImpact_ReturnsExplosionCommand() {
        Position pos = new Position(0, 0);
        Team team = new Team("TeamTest", 1, new Position[]{pos});
        Worm worm = mock(Worm.class);
        when(worm.getTeam()).thenReturn(team);

        UseParams params = new UseParams(0.0, pos, worm);
        HealingProjectile healing = new HealingProjectile(params);

        GameWorld worldMock = mock(GameWorld.class);
        PhysicsEngine physicsMock = mock(PhysicsEngine.class);
        when(worldMock.getPhysics()).thenReturn(physicsMock);
        EventBus busMock = mock(EventBus.class);
        when(worldMock.getBus()).thenReturn(busMock);

        List<GameCommand> commands = healing.onImpact(null);
        assertNotNull(commands);
        assertFalse(commands.isEmpty());
        for (GameCommand cmd : commands) {
            cmd.execute(worldMock);
        }
        verify(physicsMock).createExplosion(eq(healing), eq(0), eq(15), eq(-20), eq(0.0));
    }
}
