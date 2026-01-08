package com.lllmtb.engine.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.physics.EntityRegisteredEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.terrain.generation.LandScape;


public class GameCommandTest {
    private GameWorld world;
    private PhysicsEngine physics;
    private EventBus bus;
    private BlockingQueue<GameCommand> queue;

    @BeforeEach
    void setUp() throws Exception {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"Team"}, new boolean[1], 1, 100, 10, false, null, false, 200, LandScape.FLAT,2,GameMode.NORMAL, true, false);
        bus = mock(EventBus.class);
        world = new GameWorld(settings, bus);

        // Use reflection to inject mocks into private fields
        Field physicsField = GameWorld.class.getDeclaredField("physics");
        physicsField.setAccessible(true);
        physics = mock(PhysicsEngine.class);
        physicsField.set(world, physics);

        Field queueField = GameWorld.class.getDeclaredField("commandQueue");
        queueField.setAccessible(true);
        queue = new LinkedBlockingQueue<>();
        queueField.set(world, queue);
    }

    @Test
    void invokesPhysicsAddModifierWhenAddPhysicsModifierCommandIsExecuted() {
        Entity target = mock(Entity.class);
        PhysicsModifier modifier = mock(PhysicsModifier.class);

        AddPhysicsModifierCommand cmd = new AddPhysicsModifierCommand(target, modifier);

        cmd.execute(world);

        verify(physics).addModifier(isNull(), same(modifier));
    }

    @Test
    void postsProjectileRegisteredEventWhenRegisterProjectileCommandIsExecuted() {
        Entity entity = mock(Projectile.class);
        Entity owner = mock(Entity.class);

        RegisterEntityCommand cmd = new RegisterEntityCommand(entity, owner);

        cmd.execute(world);

        verify(physics).add(isNull());
        verify(bus).postQueued(argThat(e ->
                e instanceof EntityRegisteredEvent
                        && ((EntityRegisteredEvent) e).entity() == entity
                        && ((EntityRegisteredEvent) e).source() == owner
        ));
    }

    @Test
    void invokesPhysicsRemoveModifierWhenRemovePhysicsModifierCommandIsExecuted() {
        Entity target = mock(Entity.class);
        PhysicsModifier modifier = mock(PhysicsModifier.class);

        RemovePhysicsModifierCommand cmd = new RemovePhysicsModifierCommand(target, modifier);

        cmd.execute(world);

        verify(physics).removeModifier(isNull(), same(modifier));
    }

    @Test
    void queueCommandReturnsFalseWhenGivenNullCommand() {
        boolean result = world.queueCommand(null);
        assertFalse(result);
        assertTrue(queue.isEmpty());
    }

    @Test
    void queuedCommandsExecuteInFIFOOrderAndAreDrainedByProcessCommands() {
        List<String> executed = new CopyOnWriteArrayList<>();

        GameCommand cmd1 = w -> executed.add("first");
        GameCommand cmd2 = w -> executed.add("second");

        boolean r1 = world.queueCommand(cmd1);
        boolean r2 = world.queueCommand(cmd2);
        assertTrue(r1);
        assertTrue(r2);
        assertEquals(2, queue.size());

        world.processCommands();

        assertEquals(List.of("first", "second"), executed);
        assertEquals(0, queue.size());

        // calling processCommands again should not re-execute commands
        world.processCommands();
        assertEquals(2, executed.size());
    }

    @Test
    void queueCommandsReturnsFalseIfAnyCommandIsNull() {
        List<GameCommand> cmds = new ArrayList<>();
        cmds.add(w -> {});
        cmds.add(null);
        cmds.add(w -> {});
        boolean result = world.queueCommands(cmds);
        assertFalse(result);
        assertEquals(2, queue.size());
    }
}
