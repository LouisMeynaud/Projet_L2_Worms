package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Vector;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class JetpackToolTest {
    private final JetpackTool jetpack = new JetpackTool();

    @Test
    public void id() {
        assertEquals(ToolId.JETPACK, jetpack.id());
    }

    @Test
    public void use() throws Exception {
        Worm w = new Worm("José", mock(Team.class));
        UseParams up = new UseParams(w);

        // Check that the use method returns the correct commands
        List<GameCommand> cmds = jetpack.use(up);
        assertEquals(2, cmds.size());

        // First command should be AddPhysicsModifierCommand
        GameCommand cmd = cmds.get(0);
        assertInstanceOf(AddPhysicsModifierCommand.class, cmd);

        // Assert that the modifier has the correct properties
        Field modifierField = AddPhysicsModifierCommand.class.getDeclaredField("modifier");
        modifierField.setAccessible(true);
        PhysicsModifier m = (PhysicsModifier) modifierField.get(cmd);

        assertFalse(m.enabled); // Jetpack starts disabled
        assertFalse(m.oneShot);
        assertEquals(5.0, m.remaining); // 5 seconds of fuel
        assertEquals(jetpack.id(), m.source);

        // Test the transform function - should add upward thrust
        Vector v1 = new Vector(0, 0);
        assertEquals(20.0, m.transform.apply(v1).getY());

        Vector v2 = new Vector(5, -10);
        assertEquals(10.0, m.transform.apply(v2).getY()); // -10 + 20 = 10
        assertEquals(5.0, m.transform.apply(v2).getX()); // X should be unchanged

        // Second command should be the listener registration command
        GameCommand listenerCmd = cmds.get(1);
        assertNotNull(listenerCmd);
    }
}

