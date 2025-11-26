package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UnknownItemException;
import com.lllmtb.model.item.weapon.*;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


// TODO - Remove all the registry-related tests and move them to a more generic ItemRegistryTest unit.

public class InventoryTest {

    public static Inventory ivt;
    public static WeaponRegistry registry;
    public static GunWeapon gun = new GunWeapon();
    public static Position p = new Position(0, 0);
    public static Worm w = new Worm("José", new Team("Team", 1, new Position[]{p}));
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
    public void unlock_whenWeaponIsInRegistry() {
        ivt.unlock(gun.id());
        assertTrue(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void unlock_whenWeaponIsNotInRegistry() {
        assertThrows(UnknownItemException.class, () -> ivt.unlock(WeaponId.NONE));
    }

    @Test
    public void lock() {
        ivt.unlock(gun.id());
        ivt.lock(gun.id());
        assertFalse(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void getAmmo() {
        assertEquals(0, ivt.weapons.getAmmo(gun.id()));
    }

    @Test
    public void addAmmo_whenWeaponIsNotInRegistry() {
        assertThrows(UnknownItemException.class, () -> ivt.weapons.addAmmo(WeaponId.NONE, 0));
    }

    @Test
    public void addAmmo_whenAmountEquals0() {
        ivt.weapons.addAmmo(gun.id(), 0);
        assertEquals(0, ivt.weapons.getAmmo(gun.id()));
    }

    @Test
    public void addAmmo_whenAmountSup0() {
        ivt.weapons.addAmmo(gun.id(), 10);
        assertEquals(10, ivt.weapons.getAmmo(gun.id()));
    }

    @Test
    public void canFire_whenWeaponIsNotInRegistry() {
        assertThrows(UnknownItemException.class, () -> ivt.canFire(WeaponId.NONE));
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
        ivt.weapons.addAmmo(gun.id(), 10);
        assertTrue(ivt.canFire(gun.id()));
    }

    @Test
    public void fire_whenWeaponIsNotInRegistry() {
        assertThrows(UnknownItemException.class, () -> ivt.fire(WeaponId.NONE, fp));
    }

    @Test
    public void fire_WhenWeaponCanFire() {
        ivt.unlock(gun.id());
        ivt.weapons.addAmmo(gun.id(), 10);
        assertDoesNotThrow(() -> ivt.fire(gun.id(), fp));
        assertEquals(9, ivt.weapons.ammoSnapshot().get(gun.id()));
    }

    @Test
    public void fire_whenWeaponCanNotFire() {
        assertThrows(UnusableWeaponException.class, () -> ivt.fire(gun.id(), fp));
    }

    @Test
    public void unlockedWeapons_whenEmpty() {
        Set<WeaponId> w = new HashSet<>();
        assertEquals(w, ivt.unlockedWeapons());
    }
}