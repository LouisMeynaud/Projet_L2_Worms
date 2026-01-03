package com.lllmtb.model.item.weapon;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.ShotGunProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;

public class ShotGunWeaponTest {
    private final ShotGunWeapon b = new ShotGunWeapon();

    @Test
    public void id() {
        assertEquals(WeaponId.SHOTGUN, b.id());
    }

    @Test
    public void fire() throws Exception {
        Position p = new Position(0, 0);
        Worm w = mock(Worm.class);
        UseParams fp = new UseParams(0, p, w, 0);

        // Check that the fire method returns the correct command
        List<GameCommand> cmds = b.fire(fp);
        assertEquals(5, cmds.size());

        GameCommand cmd = cmds.get(0);
        assertInstanceOf(RegisterEntityCommand.class, cmd);

        // Assert that the projectile has the correct properties
        Field projectileField = RegisterEntityCommand.class.getDeclaredField("entity");
        projectileField.setAccessible(true);
        Object projectile = projectileField.get(cmd);
        assertInstanceOf(ShotGunProjectile.class, projectile);

        Field ownerField = RegisterEntityCommand.class.getDeclaredField("owner");
        ownerField.setAccessible(true);
        Object owner = ownerField.get(cmd);
        assertEquals(w, owner);
    }
}
