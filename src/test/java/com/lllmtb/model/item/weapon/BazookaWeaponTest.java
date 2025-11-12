package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BazookaWeaponTest {

    @Test
    public void Id() {
        BazookaWeapon b = new BazookaWeapon();
        assertEquals(WeaponId.BAZOOKA, b.id());
    }

    @Test
    public void CreateProjectile() {
        FireParams fp = Mockito.mock(FireParams.class);
        Position p = new Position(0, 0);
        FireParams fp = new FireParams(0, p, new Worm(p));
        BazookaWeapon b = new BazookaWeapon();
        assertEquals(RocketProjectile.class, (b.createProjectile(fp)).getClass());
    }
}
