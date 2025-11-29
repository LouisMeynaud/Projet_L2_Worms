package com.lllmtb.model.item;

import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.weapon.WeaponRegistry;
import com.lllmtb.model.item.weapon.GunWeapon;
import com.lllmtb.model.item.weapon.BazookaWeapon;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.tool.ToolRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRegistryTest {

    private WeaponRegistry weaponRegistry;
    private ToolRegistry toolRegistry;

    @BeforeEach
    public void setUp() {
        weaponRegistry = new WeaponRegistry();
        toolRegistry = new ToolRegistry();
    }

    // ==================== get() Tests ====================

    @Test
    public void get_withValidWeaponId() {
        assertNotNull(weaponRegistry.get(WeaponId.GUN));
        assertNotNull(weaponRegistry.get(WeaponId.BAZOOKA));
    }

    @Test
    public void get_withInvalidWeaponId() {
        assertNull(weaponRegistry.get(WeaponId.NONE));
    }

    @Test
    public void get_withValidToolId() {
        assertNotNull(toolRegistry.get(ToolId.PARACHUTE));
    }

    @Test
    public void get_returnsCorrectWeaponType() {
        assertInstanceOf(GunWeapon.class, weaponRegistry.get(WeaponId.GUN));
        assertInstanceOf(BazookaWeapon.class, weaponRegistry.get(WeaponId.BAZOOKA));
    }

    // ==================== contains() Tests ====================

    @Test
    public void contains_withValidWeaponId() {
        assertTrue(weaponRegistry.contains(WeaponId.GUN));
        assertTrue(weaponRegistry.contains(WeaponId.BAZOOKA));
    }

    @Test
    public void contains_withInvalidWeaponId() {
        assertFalse(weaponRegistry.contains(WeaponId.NONE));
    }

    @Test
    public void contains_withValidToolId() {
        assertTrue(toolRegistry.contains(ToolId.PARACHUTE));
    }

    // ==================== getAmmo() Tests ====================

    @Test
    public void getAmmo_initiallyZero() {
        assertEquals(0, weaponRegistry.getAmmo(WeaponId.GUN));
        assertEquals(0, toolRegistry.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void getAmmo_forUnknownItemReturnsZero() {
        assertEquals(0, weaponRegistry.getAmmo(WeaponId.NONE));
    }

    @Test
    public void getAmmo_afterAddingAmmo() {
        weaponRegistry.addAmmo(WeaponId.GUN, 10);
        assertEquals(10, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    // ==================== addAmmo() Tests ====================

    @Test
    public void addAmmo_positiveAmount() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        assertEquals(5, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_multipleAdditions() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        weaponRegistry.addAmmo(WeaponId.GUN, 3);
        assertEquals(8, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_zeroAmount() {
        weaponRegistry.addAmmo(WeaponId.GUN, 0);
        assertEquals(0, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_negativeAmount() {
        weaponRegistry.addAmmo(WeaponId.GUN, 10);
        weaponRegistry.addAmmo(WeaponId.GUN, -3);
        assertEquals(7, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_negativeToZero() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        weaponRegistry.addAmmo(WeaponId.GUN, -5);
        assertEquals(0, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_negativeCanBeBelowZero() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        weaponRegistry.addAmmo(WeaponId.GUN, -10);
        assertEquals(-5, weaponRegistry.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_throwsExceptionForUnknownWeapon() {
        assertThrows(UnknownItemException.class, () -> weaponRegistry.addAmmo(WeaponId.NONE, 10));
    }

    @Test
    public void addAmmo_throwsExceptionForUnknownTool() {
        assertThrows(UnknownItemException.class, () -> toolRegistry.addAmmo(ToolId.NONE, 10));
    }

    // ==================== ammoSnapshot() Tests ====================

    @Test
    public void ammoSnapshot_isUnmodifiable() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        Map<WeaponId, Integer> snapshot = weaponRegistry.ammoSnapshot();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.put(WeaponId.BAZOOKA, 10));
    }

    @Test
    public void ammoSnapshot_reflectsCurrentState() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        Map<WeaponId, Integer> snapshot = weaponRegistry.ammoSnapshot();
        assertEquals(5, snapshot.get(WeaponId.GUN));
    }

    @Test
    public void ammoSnapshot_containsAllWeapons() {
        Map<WeaponId, Integer> snapshot = weaponRegistry.ammoSnapshot();
        assertTrue(snapshot.containsKey(WeaponId.GUN));
        assertTrue(snapshot.containsKey(WeaponId.BAZOOKA));
    }

    @Test
    public void ammoSnapshot_independent() {
        weaponRegistry.addAmmo(WeaponId.GUN, 5);
        Map<WeaponId, Integer> snapshot1 = weaponRegistry.ammoSnapshot();
        weaponRegistry.addAmmo(WeaponId.GUN, 3);
        Map<WeaponId, Integer> snapshot2 = weaponRegistry.ammoSnapshot();
        
        assertEquals(5, snapshot1.get(WeaponId.GUN));
        assertEquals(8, snapshot2.get(WeaponId.GUN));
    }

    // ==================== Multi-item Tests ====================

    @Test
    public void multipleItems_independentAmmo() {
        weaponRegistry.addAmmo(WeaponId.GUN, 10);
        weaponRegistry.addAmmo(WeaponId.BAZOOKA, 5);
        
        assertEquals(10, weaponRegistry.getAmmo(WeaponId.GUN));
        assertEquals(5, weaponRegistry.getAmmo(WeaponId.BAZOOKA));
    }

    @Test
    public void multipleItems_eachCanBeModified() {
        weaponRegistry.addAmmo(WeaponId.GUN, 10);
        weaponRegistry.addAmmo(WeaponId.BAZOOKA, 5);
        weaponRegistry.addAmmo(WeaponId.GUN, -2);
        
        assertEquals(8, weaponRegistry.getAmmo(WeaponId.GUN));
        assertEquals(5, weaponRegistry.getAmmo(WeaponId.BAZOOKA));
    }
}

