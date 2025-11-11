package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BazookaWeaponTest {

    @Test
    public void testId() {
        BazookaWeapon b = new BazookaWeapon();
        assertEquals(WeaponId.BAZOOKA, b.id());
    }

    @Test
    public void testCreateProjectile() {
        FireParams fp = Mockito.mock(FireParams.class);
        BazookaWeapon b = new BazookaWeapon();
        assertEquals(BulletProjectile.class, (b.createProjectile(fp)).getClass());
    }
}
