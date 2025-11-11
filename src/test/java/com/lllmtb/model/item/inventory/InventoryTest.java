package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.item.weapon.*;
import com.lllmtb.physics.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class InventoryTest {

    public static Inventory ivt;
    public static WeaponRegistry registry;
    public static GunWeapon gun = new GunWeapon();
    public static BazookaWeapon bzk = new BazookaWeapon();
    public static Vector v = Mockito.mock(Vector.class);
    public static Entity e = Mockito.mock(Entity.class);
    public static FireParams fp = new FireParams(0, 0, v, e);

    @BeforeEach
    public void setUp() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(gun);
        registry = new WeaponRegistry(weapons);
        ivt = new Inventory(registry);
    }

    @Test
    public void testConstructeur() {
        assertEquals(registry, ivt.getRegistry());
    }

    @Test
    public void testIsUnlocked() {
        assertFalse(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void testUnlock1() {
        ivt.unlock(gun.id());
        assertTrue(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void testUnlock2() {
        try {
            ivt.unlock(bzk.id());
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + bzk.id(), e.getMessage());
        }
    }

    @Test
    public void testLock() {
        ivt.unlock(gun.id());
        ivt.lock(gun.id());
        assertFalse(ivt.isUnlocked(gun.id()));
    }

    @Test
    public void testGetAmmo() {
        assertEquals(0, ivt.getAmmo(gun.id()));
    }

    @Test
    public void testAddAmmo1() {
        try {
            ivt.addAmmo(bzk.id(), 0);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + bzk.id(), e.getMessage());
        }
    }

    @Test
    public void testAddAmmo2() {
        ivt.addAmmo(gun.id(), 0);
        assertEquals(0, ivt.getAmmo(gun.id()));
    }

    @Test
    public void testAddAmmo3() {
        ivt.addAmmo(gun.id(), 10);
        assertEquals(10, ivt.getAmmo(gun.id()));
    }

    @Test
    public void testCanFire1() {
        try {
            ivt.canFire(bzk.id());
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon: " + bzk.id(), e.getMessage());
        }
    }

    @Test
    public void testCanFire2() {
        assertFalse(ivt.canFire(gun.id()));
    }

    @Test
    public void testCanFire3() {
        ivt.unlock(gun.id());
        assertFalse(ivt.canFire(gun.id()));
    }

    @Test
    public void testCanFire4() {
        ivt.unlock(gun.id());
        ivt.addAmmo(gun.id(), 10);
        assertTrue(ivt.canFire(gun.id()));
    }

    @Test
    public void testFire1() {
        try {
            ivt.fire(bzk.id(), fp);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown weapon : " + bzk.id(), e.getMessage());
        }
    }

    @Test
    public void testFire2() {
        ivt.unlock(gun.id());
        ivt.addAmmo(gun.id(), 10);
        assertEquals(BulletProjectile.class, (ivt.fire(gun.id(), fp).getClass()));
    }

    @Test
    public void testFire3() {
        try{
            ivt.fire(gun.id(), fp);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Weapon can't fire : " + gun.id(), e.getMessage());
        }
    }

    @Test
    public void testUnlockedWeapons() {
        Set<WeaponId> w = new HashSet<>();
        assertEquals(w, ivt.unlockedWeapons());
    }

    @Test
    public void testAmmoSnapshot() {
        Map<WeaponId, Integer> m = new HashMap<>();
        assertEquals(m, ivt.ammoSnapshot());
    }
}