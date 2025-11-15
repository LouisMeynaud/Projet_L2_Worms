package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.model.entity.team.Team;
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
        BazookaWeapon b = new BazookaWeapon();
        Position p = new Position(0, 0);
        Worm w = new Worm("José", new Team("Team", 1, p), p);
        FireParams fp = new FireParams(0, p, w);
        assertEquals(RocketProjectile.class, (b.createProjectile(fp)).getClass());
    }
}
