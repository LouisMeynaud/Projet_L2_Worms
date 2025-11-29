package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

public class ParachuteToolTest {
    private final ParachuteTool parachute = new ParachuteTool();

    @Test
    public void Id() {
        assertEquals(ToolId.PARACHUTE, parachute.id());
    }

    @Test
    public void fire() {
        Worm w = mock(Worm.class);
        UseParams up = new UseParams(w);

        // Check that the use method returns the correct command
        List<GameCommand> cmds = parachute.use(up);
        assertEquals(1, cmds.size());

        GameCommand cmd = cmds.get(0);
        assertInstanceOf(AddPhysicsModifierCommand.class, cmd);
    }
}
