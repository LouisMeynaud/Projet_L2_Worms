package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.*;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class InventoryTest {

    public static Inventory ivt;
    public static WeaponRegistry registry;
    public static GunWeapon gun = new GunWeapon();
    public static Position p = new Position(0, 0);
    public static Worm w = new Worm("José", new Team("Team", 1, p));
    public static FireParams fp = new FireParams(0, p, w);

    @BeforeEach
    public void setUp() {
        registry = new WeaponRegistry();
        ivt = new Inventory();
    }

    @Test
    public void isUnlocked() {
        assertFalse(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void unlock_whenWeaponIsInRegistory() {
        ivt.unlock(gun.id());
        assertTrue(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void unlock_whenWeaponIsNotInRegistory() {
        try {
            ivt.unlock(WeaponId.NONE);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + WeaponId.NONE, e.getMessage());
        }
    }

    @Test
    public void lock() {
        ivt.unlock(gun.id());
        ivt.lock(gun.id());
        assertFalse(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void getAmmo() {
        assertEquals(0, ivt.getAmmo(gun.id()));
    }

    @Test
    public void addAmmo_whenWeaponIsNotInRegistery() {
        try {
            ivt.addAmmo(WeaponId.NONE, 0);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + WeaponId.NONE, e.getMessage());
        }
    }

    @Test
    public void addAmmo_whenAmoutequals0() {
        ivt.addAmmo(gun.id(), 0);
        assertEquals(0, ivt.getAmmo(gun.id()));
    }

    @Test
    public void addAmmo_whenAmoutSup0() {
        ivt.addAmmo(gun.id(), 10);
        assertEquals(10, ivt.getAmmo(gun.id()));
    }

    @Test
    public void canFire_whenWeaponIsNotInRegistory() {
        try {
            ivt.canFire(WeaponId.NONE);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + WeaponId.NONE, e.getMessage());
        }
    }

    @Test
    public void canFire_whenWeaponIsLocked() {
        assertFalse(ivt.canFire(gun.id()));
    }

    @Test
    public void canFire_whenWeaponHas0Ammo() {
        ivt.unlock(gun.id());
        assertFalse(ivt.canFire(gun.id()));
    }

    @Test
    public void canFire_whenWeaponCanFire() {
        ivt.unlock(gun.id());
        ivt.addAmmo(gun.id(), 10);
        assertTrue(ivt.canFire(gun.id()));
    }

    @Test
    public void fire_whenWeaponIsNotInRegistory() {
        try {
            ivt.fire(WeaponId.NONE, fp);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
        assertEquals("Unknown weapon : " + WeaponId.NONE, e.getMessage());
        }
    }

    @Test
    public void fire_WhenWeaponCanFire() {
        ivt.unlock(gun.id());
        ivt.addAmmo(gun.id(), 10);
        assertEquals(BulletProjectile.class, (ivt.fire(gun.id(), fp).getClass()));
        assertEquals(9, ivt.ammoSnapshot().get(gun.id()));
    }

    @Test
    public void fire_whenWeaponCanNotFire() {
        try{
            ivt.fire(gun.id(), fp);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Weapon can't fire : " + gun.id(), e.getMessage());
        }
    }

    @Test
    public void UnlockedWeapons() {
        Set<WeaponId> w = new HashSet<>();
        assertEquals(w, ivt.unlockedWeapons());
    }
}