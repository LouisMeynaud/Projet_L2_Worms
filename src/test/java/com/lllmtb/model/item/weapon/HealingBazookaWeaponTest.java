package com.lllmtb.model.item.weapon;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.HealingProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;

public class HealingBazookaWeaponTest {
    private final HealingBazookaWeapon b = new HealingBazookaWeapon();

    @Test
    public void id() {
        assertEquals(WeaponId.HEALINGBAZOOKA, b.id());
    }

    @Test
    public void fire() throws Exception {
        Position p = new Position(0, 0);
        Worm w = mock(Worm.class);
        UseParams fp = new UseParams(0, p, w);

        // Check that the fire method returns the correct command
        List<GameCommand> cmds = b.fire(fp);
        assertEquals(1, cmds.size());

        GameCommand cmd = cmds.get(0);
        assertInstanceOf(RegisterProjectileCommand.class, cmd);

        // Assert that the projectile has the correct properties
        Field projectileField = RegisterProjectileCommand.class.getDeclaredField("projectile");
        projectileField.setAccessible(true);
        Object projectile = projectileField.get(cmd);
        assertInstanceOf(HealingProjectile.class, projectile);

        Field ownerField = RegisterProjectileCommand.class.getDeclaredField("owner");
        ownerField.setAccessible(true);
        Object owner = ownerField.get(cmd);
        assertEquals(w, owner);
    }
}
