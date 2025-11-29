package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

public class BazookaWeaponTest {
    private final BazookaWeapon b = new BazookaWeapon();

    @Test
    public void id() {
        assertEquals(WeaponId.BAZOOKA, b.id());
    }

    @Test
    public void fire() throws Exception {
        Position p = new Position(0, 0);
        Worm w = mock(Worm.class);
        FireParams fp = new FireParams(0, p, w);

        // Check that the fire method returns the correct command
        List<GameCommand> cmds = b.fire(fp);
        assertEquals(1, cmds.size());

        GameCommand cmd = cmds.get(0);
        assertInstanceOf(RegisterProjectileCommand.class, cmd);

        // Assert that the projectile has the correct properties
        Field projectileField = RegisterProjectileCommand.class.getDeclaredField("projectile");
        projectileField.setAccessible(true);
        Object projectile = projectileField.get(cmd);
        assertInstanceOf(RocketProjectile.class, projectile);

        Field ownerField = RegisterProjectileCommand.class.getDeclaredField("owner");
        ownerField.setAccessible(true);
        Object owner = ownerField.get(cmd);
        assertEquals(w, owner);
    }
}
