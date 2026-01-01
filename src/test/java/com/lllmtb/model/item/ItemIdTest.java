package com.lllmtb.model.item;

import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ItemId interface and its implementations (ToolId and WeaponId).
 * Verifies the usesCrosshair() functionality.
 */
public class ItemIdTest {

    // tools

    @Test
    public void toolId_callStrike_usesCrosshair() {
        assertTrue(ToolId.CALLSTRIKE.usesCrosshair(),
                "CALLSTRIKE should use crosshair targeting");
    }

    @Test
    public void toolId_parachute_doesNotUseCrosshair() {
        assertFalse(ToolId.PARACHUTE.usesCrosshair(),
                "PARACHUTE should not use crosshair targeting");
    }

    @Test
    public void toolId_jetpack_doesNotUseCrosshair() {
        assertFalse(ToolId.JETPACK.usesCrosshair(),
                "JETPACK should not use crosshair targeting");
    }

    @Test
    public void toolId_none_doesNotUseCrosshair() {
        assertFalse(ToolId.NONE.usesCrosshair(),
                "NONE should not use crosshair targeting");
    }

    // weapons

    @Test
    public void weaponId_gun_doesNotUseCrosshair() {
        assertFalse(WeaponId.GUN.usesCrosshair(),
                "GUN should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_bazooka_doesNotUseCrosshair() {
        assertFalse(WeaponId.BAZOOKA.usesCrosshair(),
                "BAZOOKA should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_healingBazooka_doesNotUseCrosshair() {
        assertFalse(WeaponId.HEALINGBAZOOKA.usesCrosshair(),
                "HEALINGBAZOOKA should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_shotgun_doesNotUseCrosshair() {
        assertFalse(WeaponId.SHOTGUN.usesCrosshair(),
                "SHOTGUN should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_electronCanon_doesNotUseCrosshair() {
        assertFalse(WeaponId.ELECTRONCANON.usesCrosshair(),
                "ELECTRONCANON should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_gasGun_doesNotUseCrosshair() {
        assertFalse(WeaponId.GASGUN.usesCrosshair(),
                "GASGUN should not use crosshair targeting by default");
    }

    @Test
    public void weaponId_none_doesNotUseCrosshair() {
        assertFalse(WeaponId.NONE.usesCrosshair(),
                "NONE should not use crosshair targeting");
    }

    @Test
    public void itemId_defaultImplementation_returnsFalse() {
        // Create a test implementation of ItemId that doesn't override usesCrosshair
        ItemId defaultItem = new ItemId() {
        };

        assertFalse(defaultItem.usesCrosshair(),
                "Default ItemId implementation should return false");
    }
}

