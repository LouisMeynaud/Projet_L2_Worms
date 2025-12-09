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

public class ParachuteToolTest {
    private final ParachuteTool parachute = new ParachuteTool();

    @Test
    public void id() {
        assertEquals(ToolId.PARACHUTE, parachute.id());
    }

    @Test
    public void use() throws Exception {
        Worm w = new Worm("José", mock(Team.class));
        UseParams up = new UseParams(w);

        // Check that the use method returns the correct command
        List<GameCommand> cmds = parachute.use(up);
        assertEquals(1, cmds.size());

        GameCommand cmd = cmds.get(0);
        assertInstanceOf(AddPhysicsModifierCommand.class, cmd);

        // Assert that the modifier has the correct properties
        Field modifierField = AddPhysicsModifierCommand.class.getDeclaredField("modifier");
        modifierField.setAccessible(true);
        PhysicsModifier m = (PhysicsModifier) modifierField.get(cmd);

        assertTrue(m.enabled);
        assertFalse(m.oneShot);
        assertTrue(Double.isInfinite(m.remaining));
        assertEquals(parachute.id(), m.source);

        // Test the transform function
        Vector v1 = new Vector(0, -10);
        assertEquals(-3.0, m.transform.apply(v1).getY());

        Vector v2 = new Vector(0, 5);
        assertEquals(5.0, m.transform.apply(v2).getY());
    }
}
