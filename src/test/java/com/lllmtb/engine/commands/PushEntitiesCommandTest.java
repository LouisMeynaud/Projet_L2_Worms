package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PushEntitiesCommandTest {
    private GameWorld world;
    private EventBus bus;
    private Team team1;
    private Team team2;
    private Worm ownerWorm;
    private Worm targetWorm;
    private Position centerPos;

    @BeforeEach
    void setUp() {
        GameSettings settings = new GameSettings(
            "test",
            2,
            new String[]{"Team1", "Team2"},
            1,
            100,
            10,
            false,
            null,
            false,
            200,
            LandScape.FLAT,
            2,
            GameMode.NORMAL,
            true,
            false
        );
        bus = mock(EventBus.class);
        world = new GameWorld(settings, bus);

        // Get the teams created by GameWorld
        team1 = world.getTeams().get(0);
        team2 = world.getTeams().get(1);

        ownerWorm = team1.getTeamWorms().get(0);
        targetWorm = team2.getTeamWorms().get(0);

        centerPos = new Position(100, 100);
    }

    @Test
    void testConstructorStoresParameters() {
        double angle = Math.PI / 4; // 45 degrees
        double radius = 50.0;
        double force = 25.0;

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, angle, radius, force, ownerWorm);

        assertNotNull(cmd);
    }

    @Test
    void testConstructorThrowsNullPointerExceptionForNullPosition() {
        assertThrows(NullPointerException.class, () ->
            new PushEntitiesCommand(null, 0, 50, 25, ownerWorm)
        );
    }

    @Test
    void testConstructorThrowsNullPointerExceptionForNullOwner() {
        assertThrows(NullPointerException.class, () ->
            new PushEntitiesCommand(centerPos, 0, 50, 25, null)
        );
    }

    @Test
    void testExecutePushesEntityInSwingDirection() {
        // Place target worm to the right of center (at 45 degrees)
        Position targetPos = new Position(130, 130);
        targetWorm.spawn(targetPos);

        // Swing at 45 degrees (Math.PI / 4)
        double angle = Math.PI / 4;
        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, angle, 50, 30, ownerWorm);

        Vector initialVelocity = targetWorm.getBody().getVector();

        cmd.execute(world);

        Vector newVelocity = targetWorm.getBody().getVector();

        // Verify velocity changed (entity was pushed)
        assertNotEquals(initialVelocity.getX(), newVelocity.getX());
        assertNotEquals(initialVelocity.getY(), newVelocity.getY());

        // Verify worm is no longer on ground after being pushed
        assertFalse(targetWorm.isOnGround());
    }

    @Test
    void testExecuteDoesNotPushOwner() {
        // Place owner at a position within push radius
        Position ownerPos = new Position(120, 120);
        ownerWorm.spawn(ownerPos);

        Vector initialVelocity = ownerWorm.getBody().getVector().copy();

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, Math.PI / 4, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector newVelocity = ownerWorm.getBody().getVector();

        // Owner should not be pushed
        assertEquals(initialVelocity.getX(), newVelocity.getX(), 0.001);
        assertEquals(initialVelocity.getY(), newVelocity.getY(), 0.001);
    }

    @Test
    void testExecuteDoesNotPushEntitiesOutsideRadius() {
        // Place target worm far from center (outside radius)
        Position farPos = new Position(200, 200);
        targetWorm.spawn(farPos);

        Vector initialVelocity = targetWorm.getBody().getVector().copy();

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, Math.PI / 4, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector newVelocity = targetWorm.getBody().getVector();

        // Target should not be pushed (too far)
        assertEquals(initialVelocity.getX(), newVelocity.getX(), 0.001);
        assertEquals(initialVelocity.getY(), newVelocity.getY(), 0.001);
    }

    @Test
    void testExecuteDoesNotPushEntitiesBehindSwing() {
        // Place target worm behind the swing direction
        Position behindPos = new Position(70, 100); // To the left when swinging right
        targetWorm.spawn(behindPos);

        Vector initialVelocity = targetWorm.getBody().getVector().copy();

        // Swing to the right (0 radians)
        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector newVelocity = targetWorm.getBody().getVector();

        // Target should not be pushed (behind swing)
        assertEquals(initialVelocity.getX(), newVelocity.getX(), 0.001);
        assertEquals(initialVelocity.getY(), newVelocity.getY(), 0.001);
    }

    @Test
    void testExecutePushForceFallsOffWithDistance() {
        // Create two targets at different distances
        Position closePos = new Position(110, 100); // Very close
        Position farPos = new Position(140, 100);   // Further away

        targetWorm.spawn(closePos);

        // Create a second target worm
        Worm farWorm = new Worm("FarWorm", team2);
        farWorm.spawn(farPos);
        world.registerEntity(farWorm);

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector closeVelocity = targetWorm.getBody().getVector();
        Vector farVelocity = farWorm.getBody().getVector();

        // Closer entity should have greater velocity change
        double closeMagnitude = Math.sqrt(closeVelocity.getX() * closeVelocity.getX() + closeVelocity.getY() * closeVelocity.getY());
        double farMagnitude = Math.sqrt(farVelocity.getX() * farVelocity.getX() + farVelocity.getY() * farVelocity.getY());

        assertTrue(closeMagnitude > farMagnitude, "Closer entity should be pushed harder");
    }

    @Test
    void testExecuteWithZeroAnglePushesRight() {
        // Place target to the right
        Position rightPos = new Position(130, 100);
        targetWorm.spawn(rightPos);

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector velocity = targetWorm.getBody().getVector();

        // Should push to the right (positive x velocity)
        assertTrue(velocity.getX() > 0, "Should push to the right");
    }

    @Test
    void testExecuteWithPiAnglePushesLeft() {
        // Place target to the left
        Position leftPos = new Position(70, 100);
        targetWorm.spawn(leftPos);

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, Math.PI, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector velocity = targetWorm.getBody().getVector();

        // Should push to the left (negative x velocity)
        assertTrue(velocity.getX() < 0, "Should push to the left");
    }

    @Test
    void testExecuteWithHalfPiAnglePushesUp() {
        // Place target above
        Position upPos = new Position(100, 130);
        targetWorm.spawn(upPos);

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, Math.PI / 2, 50, 30, ownerWorm);
        cmd.execute(world);

        Vector velocity = targetWorm.getBody().getVector();

        // Should push upward (positive y velocity)
        assertTrue(velocity.getY() > 0, "Should push upward");
    }

    @Test
    void testExecuteWithMultipleEntitiesInRange() {
        // Place multiple worms in range
        Position pos1 = new Position(120, 100);
        Position pos2 = new Position(130, 110);

        targetWorm.spawn(pos1);

        Worm secondWorm = new Worm("SecondWorm", team2);
        secondWorm.spawn(pos2);
        world.registerEntity(secondWorm);

        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 50, 30, ownerWorm);
        cmd.execute(world);

        // Both should be pushed
        assertNotEquals(0, targetWorm.getBody().getVector().getX(), 0.001);
        assertNotEquals(0, secondWorm.getBody().getVector().getX(), 0.001);

        assertFalse(targetWorm.isOnGround());
        assertFalse(secondWorm.isOnGround());
    }

    @Test
    void testExecuteWithVerySmallRadius() {
        // Place target just outside a tiny radius
        Position nearPos = new Position(102, 100);
        targetWorm.spawn(nearPos);

        Vector initialVelocity = targetWorm.getBody().getVector().copy();

        // Use very small radius (1 unit)
        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 1, 30, ownerWorm);
        cmd.execute(world);

        Vector newVelocity = targetWorm.getBody().getVector();

        // Should not be pushed (outside tiny radius)
        assertEquals(initialVelocity.getX(), newVelocity.getX(), 0.001);
    }

    @Test
    void testExecuteWithZeroForce() {
        Position nearPos = new Position(110, 100);
        targetWorm.spawn(nearPos);

        Vector initialVelocity = targetWorm.getBody().getVector().copy();

        // Use zero force
        PushEntitiesCommand cmd = new PushEntitiesCommand(centerPos, 0, 50, 0, ownerWorm);
        cmd.execute(world);

        Vector newVelocity = targetWorm.getBody().getVector();

        // Should not be pushed significantly (zero force)
        assertEquals(initialVelocity.getX(), newVelocity.getX(), 0.001);
    }
}

