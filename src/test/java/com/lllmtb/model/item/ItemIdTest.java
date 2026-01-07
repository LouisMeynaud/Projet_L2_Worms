package com.lllmtb.model.item;

import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ItemId interface and its implementations (ToolId and WeaponId).
 * Verifies the getShootMode() functionality and other item properties.
 */
public class ItemIdTest {

    // tools

    @Test
    public void toolId_callStrike_usesCrosshairMode() {
        assertEquals(ShootMode.CROSSHAIR, ToolId.CALLSTRIKE.getShootMode(),
                "CALLSTRIKE should use CROSSHAIR shoot mode");
    }

    @Test
    public void toolId_catConcrete_usesCrosshairMode() {
        assertEquals(ShootMode.CROSSHAIR, ToolId.CATCONCRETE.getShootMode(),
                "CATCONCRETE should use CROSSHAIR shoot mode");
    }

    @Test
    public void toolId_parachute_usesAimMode() {
        assertEquals(ShootMode.NONE, ToolId.PARACHUTE.getShootMode(),
                "PARACHUTE should use AIM shoot mode");
    }

    @Test
    public void toolId_jetpack_usesAimMode() {
        assertEquals(ShootMode.NONE, ToolId.JETPACK.getShootMode(),
                "JETPACK should use AIM shoot mode");
    }

    @Test
    public void toolId_tower_usesAimMode() {
        assertEquals(ShootMode.AIM, ToolId.TOWER.getShootMode(),
                "TOWER should use AIM shoot mode");
    }

    @Test
    public void toolId_mine_usesAimMode() {
        assertEquals(ShootMode.AIM, ToolId.MINE.getShootMode(),
                "MINE should use AIM shoot mode");
    }

    @Test
    public void toolId_grenade_usesAimMode() {
        assertEquals(ShootMode.AIM, ToolId.GRENADE.getShootMode(),
                "GRENADE should use AIM shoot mode");
    }

    @Test
    public void toolId_medkit_usesNoneMode() {
        assertEquals(ShootMode.NONE, ToolId.MEDKIT.getShootMode(),
                "MEDKIT should use NONE shoot mode");
    }

    @Test
    public void toolId_none_usesAimMode() {
        assertEquals(ShootMode.AIM, ToolId.NONE.getShootMode(),
                "NONE should use AIM shoot mode by default");
    }

    // weapons

    @Test
    public void weaponId_gun_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.GUN.getShootMode(),
                "GUN should use AIM shoot mode");
    }

    @Test
    public void weaponId_bow_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.BOW.getShootMode(),
                "BOW should use AIM shoot mode");
    }

    @Test
    public void weaponId_bazooka_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.BAZOOKA.getShootMode(),
                "BAZOOKA should use AIM shoot mode");
    }

    @Test
    public void weaponId_healingBazooka_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.HEALINGBAZOOKA.getShootMode(),
                "HEALINGBAZOOKA should use AIM shoot mode");
    }

    @Test
    public void weaponId_shotgun_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.SHOTGUN.getShootMode(),
                "SHOTGUN should use AIM shoot mode");
    }

    @Test
    public void weaponId_electronCanon_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.ELECTRONCANON.getShootMode(),
                "ELECTRONCANON should use AIM shoot mode");
    }

    @Test
    public void weaponId_gasGun_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.GASGUN.getShootMode(),
                "GASGUN should use AIM shoot mode");
    }

    @Test
    public void weaponId_none_usesAimMode() {
        assertEquals(ShootMode.AIM, WeaponId.NONE.getShootMode(),
                "NONE should use AIM shoot mode by default");
    }

    @Test
    public void getMaxChargeTime_Bow() {
        assertEquals(3000, WeaponId.BOW.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_Bazooka() {
        assertEquals(1000, WeaponId.BAZOOKA.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_HealingBazooka() {
        assertEquals(1000, WeaponId.HEALINGBAZOOKA.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_Tower() {
        assertEquals(3000, ToolId.TOWER.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_Mine() {
        assertEquals(3000, ToolId.MINE.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_Grenade() {
        assertEquals(4000, ToolId.GRENADE.getMaxChargeTime());
    }

    @Test
    public void getMaxChargeTime_Gun_NoCharge() {
        assertEquals(0, WeaponId.GUN.getMaxChargeTime());
    }
}

