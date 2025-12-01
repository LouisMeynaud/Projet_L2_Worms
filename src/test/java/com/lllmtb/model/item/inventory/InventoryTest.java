package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.*;
import com.lllmtb.model.item.tool.*;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class InventoryTest {
    private Position testPosition;
    private Team testTeam;
    private Worm testWorm;
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        testPosition = new Position(0, 0);
        testTeam = new Team("TestTeam", 1, new Position[]{testPosition});
        testWorm = testTeam.getTeamWorms().get(0);
        inventory = new Inventory();
    }

    // ==================== Weapon Unlock/Lock Tests ====================

    @Test
    public void isUnlocked_initiallyFalse() {
        assertFalse(inventory.isUnlocked(WeaponId.GUN));
        assertFalse(inventory.isUnlocked(WeaponId.BAZOOKA));
    }

    @Test
    public void unlock_makesWeaponUnlocked() {
        inventory.unlock(WeaponId.GUN);
        assertTrue(inventory.isUnlocked(WeaponId.GUN));
    }

    @Test
    public void unlock_throwsExceptionForUnknownWeapon() {
        assertThrows(UnknownWeaponException.class, () -> inventory.unlock(WeaponId.NONE));
    }

    @Test
    public void lock_removesUnlocked() {
        inventory.unlock(WeaponId.GUN);
        inventory.lock(WeaponId.GUN);
        assertFalse(inventory.isUnlocked(WeaponId.GUN));
    }

    @Test
    public void lock_doesNotThrowForLockedWeapon() {
        assertDoesNotThrow(() -> inventory.lock(WeaponId.GUN));
    }

    @Test
    public void unlock_multipleWeapons() {
        inventory.unlock(WeaponId.GUN);
        inventory.unlock(WeaponId.BAZOOKA);
        assertTrue(inventory.isUnlocked(WeaponId.GUN));
        assertTrue(inventory.isUnlocked(WeaponId.BAZOOKA));
    }

    // ==================== canFire() Tests ====================

    @Test
    public void canFire_whenWeaponIsNotInRegistry() {
        assertThrows(UnknownWeaponException.class, () -> inventory.canFire(WeaponId.NONE));
    }

    @Test
    public void canFire_whenWeaponIsLocked() {
        assertFalse(inventory.canFire(WeaponId.GUN));
    }

    @Test
    public void canFire_whenUnlockedButNoAmmo() {
        inventory.unlock(WeaponId.GUN);
        assertFalse(inventory.canFire(WeaponId.GUN));
    }

    @Test
    public void canFire_whenUnlockedAndHasAmmo() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 1);
        assertTrue(inventory.canFire(WeaponId.GUN));
    }

    @Test
    public void canFire_whenUnlockedAndHasMultipleAmmo() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        assertTrue(inventory.canFire(WeaponId.GUN));
    }

    // ==================== fire() Tests ====================

    @Test
    public void fire_throwsExceptionForUnknownWeapon() {
        FireParams params = new FireParams(0, testPosition, testWorm);
        assertThrows(UnknownWeaponException.class, () -> inventory.fire(WeaponId.NONE, params));
    }

    @Test
    public void fire_throwsExceptionWhenWeaponIsLocked() {
        FireParams params = new FireParams(0, testPosition, testWorm);
        assertThrows(UnusableWeaponException.class, () -> inventory.fire(WeaponId.GUN, params));
    }

    @Test
    public void fire_throwsExceptionWhenNoAmmo() {
        inventory.unlock(WeaponId.GUN);
        FireParams params = new FireParams(0, testPosition, testWorm);
        assertThrows(UnusableWeaponException.class, () -> inventory.fire(WeaponId.GUN, params));
    }

    @Test
    public void fire_successfullyFires() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        FireParams params = new FireParams(0, testPosition, testWorm);
        
        assertDoesNotThrow(() -> inventory.fire(WeaponId.GUN, params));
    }

    @Test
    public void fire_consumesAmmo() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        FireParams params = new FireParams(0, testPosition, testWorm);
        
        inventory.fire(WeaponId.GUN, params);
        assertEquals(9, inventory.weapons.getAmmo(WeaponId.GUN));
    }

    @Test
    public void fire_consumesMultipleAmmo() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        FireParams params = new FireParams(0, testPosition, testWorm);
        
        inventory.fire(WeaponId.GUN, params);
        inventory.fire(WeaponId.GUN, params);
        assertEquals(8, inventory.weapons.getAmmo(WeaponId.GUN));
    }

    @Test
    public void fire_returnsGameCommands() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        FireParams params = new FireParams(0, testPosition, testWorm);
        
        var result = inventory.fire(WeaponId.GUN, params);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== unlockedWeapons() Tests ====================

    @Test
    public void unlockedWeapons_initiallyEmpty() {
        Set<WeaponId> unlocked = inventory.unlockedWeapons();
        assertTrue(unlocked.isEmpty());
    }

    @Test
    public void unlockedWeapons_afterUnlockingOne() {
        inventory.unlock(WeaponId.GUN);
        Set<WeaponId> unlocked = inventory.unlockedWeapons();
        assertTrue(unlocked.contains(WeaponId.GUN));
        assertEquals(1, unlocked.size());
    }

    @Test
    public void unlockedWeapons_afterUnlockingMultiple() {
        inventory.unlock(WeaponId.GUN);
        inventory.unlock(WeaponId.BAZOOKA);
        Set<WeaponId> unlocked = inventory.unlockedWeapons();
        assertTrue(unlocked.contains(WeaponId.GUN));
        assertTrue(unlocked.contains(WeaponId.BAZOOKA));
        assertEquals(2, unlocked.size());
    }

    @Test
    public void unlockedWeapons_isUnmodifiable() {
        inventory.unlock(WeaponId.GUN);
        Set<WeaponId> unlocked = inventory.unlockedWeapons();
        assertThrows(UnsupportedOperationException.class, () -> unlocked.add(WeaponId.BAZOOKA));
    }

    @Test
    public void unlockedWeapons_afterLocking() {
        inventory.unlock(WeaponId.GUN);
        inventory.unlock(WeaponId.BAZOOKA);
        inventory.lock(WeaponId.GUN);
        Set<WeaponId> unlocked = inventory.unlockedWeapons();
        assertFalse(unlocked.contains(WeaponId.GUN));
        assertTrue(unlocked.contains(WeaponId.BAZOOKA));
    }

    // ==================== Tool canUse() Tests ====================

    @Test
    public void canUse_throwsExceptionForUnknownTool() {
        assertThrows(UnknownToolException.class, () -> inventory.canUse(ToolId.NONE));
    }

    @Test
    public void canUse_whenToolHasNoAmmo() {
        assertFalse(inventory.canUse(ToolId.PARACHUTE));
    }

    @Test
    public void canUse_whenToolHasAmmo() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 1);
        assertTrue(inventory.canUse(ToolId.PARACHUTE));
    }

    @Test
    public void canUse_whenToolHasMultipleAmmo() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        assertTrue(inventory.canUse(ToolId.PARACHUTE));
    }

    // ==================== use() Tests ====================

    @Test
    public void use_throwsExceptionForUnknownTool() {
        UseParams params = new UseParams(testWorm);
        assertThrows(UnknownToolException.class, () -> inventory.use(ToolId.NONE, params));
    }

    @Test
    public void use_throwsExceptionWhenToolHasNoAmmo() {
        UseParams params = new UseParams(testWorm);
        assertThrows(UnusableToolException.class, () -> inventory.use(ToolId.PARACHUTE, params));
    }

    @Test
    public void use_successfullyUses() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        UseParams params = new UseParams(testWorm);
        
        assertDoesNotThrow(() -> inventory.use(ToolId.PARACHUTE, params));
    }

    @Test
    public void use_consumesAmmo() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        UseParams params = new UseParams(testWorm);
        
        inventory.use(ToolId.PARACHUTE, params);
        assertEquals(4, inventory.tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void use_consumesMultipleAmmo() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        UseParams params = new UseParams(testWorm);
        
        inventory.use(ToolId.PARACHUTE, params);
        inventory.use(ToolId.PARACHUTE, params);
        assertEquals(3, inventory.tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void use_returnsGameCommands() {
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        UseParams params = new UseParams(testWorm);
        
        var result = inventory.use(ToolId.PARACHUTE, params);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== Integration Tests ====================

    @Test
    public void weaponsAndTools_independentAmmo() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        
        assertEquals(10, inventory.weapons.getAmmo(WeaponId.GUN));
        assertEquals(5, inventory.tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void weaponsAndTools_canBothBeUsed() {
        inventory.unlock(WeaponId.GUN);
        inventory.weapons.addAmmo(WeaponId.GUN, 10);
        inventory.tools.addAmmo(ToolId.PARACHUTE, 5);
        
        FireParams fireParams = new FireParams(0, testPosition, testWorm);
        UseParams useParams = new UseParams(testWorm);
        
        assertDoesNotThrow(() -> inventory.fire(WeaponId.GUN, fireParams));
        assertDoesNotThrow(() -> inventory.use(ToolId.PARACHUTE, useParams));
    }
}