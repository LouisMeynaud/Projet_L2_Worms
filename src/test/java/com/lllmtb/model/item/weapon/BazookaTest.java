package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BazookaTest {

    @Test
    public void testCreateProjectile() {
        FireParams fp = Mockito.mock(FireParams.class);
        BazookaWeapon b = new BazookaWeapon();
        assertEquals(BulletProjectile.class, (b.createProjectile(fp)).getClass());
    }
}
