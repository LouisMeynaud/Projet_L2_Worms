package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.MissileProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CallStrikeToolTest {
    private final CallStrikeTool callstrike = new CallStrikeTool();

    @Test
    public void id() {
        assertEquals(ToolId.CALLSTRIKE, callstrike.id());
    }

    @Test
    public void use() throws Exception {
        Worm w = new Worm("José", mock(Team.class));
        Position pos = new Position(10.0, 20.0);
        UseParams up = new UseParams(90, pos, w);

        // Check that the use method returns the correct commands
        List<GameCommand> cmds = callstrike.use(up);
        assertEquals(1, cmds.size());

        // First command should be RegisterProjectileCommand
        GameCommand cmd = cmds.get(0);
        assertInstanceOf(RegisterProjectileCommand.class, cmd);

        // Assert that the modifier has the correct properties
        Field ownerField = RegisterProjectileCommand.class.getDeclaredField("owner");
        ownerField.setAccessible(true);
        Worm o = (Worm) ownerField.get(cmd);

        assertEquals(w, o);

        Field projectileField = RegisterProjectileCommand.class.getDeclaredField("projectile");
        projectileField.setAccessible(true);
        Projectile p = (Projectile) projectileField.get(cmd);

        assertInstanceOf(MissileProjectile.class, p);
        assertEquals(pos, p.getBody().getPos());
        assertEquals(w, p.getOwner());
    }
}

