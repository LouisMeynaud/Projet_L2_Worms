package com.lllmtb.model.entity.team;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.tool.UnknownToolException;
import com.lllmtb.model.item.tool.UnusableToolException;
import com.lllmtb.model.item.weapon.LockedWeaponException;
import com.lllmtb.model.item.weapon.UnknownWeaponException;
import com.lllmtb.model.item.weapon.UnusableWeaponException;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;

public class TeamTest {
    private Team team;
    private Position[] spawnPositions;

    @BeforeEach
    public void setUp() {
        Team.getChosenTeamColors().clear();
        spawnPositions = new Position[]{new Position(0, 0)};
        team = new Team("TestTeam", 1, spawnPositions);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void constructor_createsTeamWithGivenName() {
        assertEquals("TestTeam", team.name);
    }

    @Test
    public void constructor_createsTeamWithUniqueId() {
        Team.getChosenTeamColors().clear();
        Team team2 = new Team("Team2", 1, new Position[]{new Position(0, 0)});
        assertNotEquals(team.id, team2.id);
    }

    @Test
    public void constructor_createsInventory() {
        assertNotNull(team.getInventory());
    }

    @Test
    public void constructor_assignsTeamColor() {
        assertNotNull(team.teamColor);
        assertEquals(TeamColor.class, team.teamColor.getClass());
    }

    @Test
    public void constructor_initializesCorrectNumberOfWorms() {
        assertEquals(1, team.getNumberOfWorms());
    }

    @Test
    public void constructor_multipleWorms() {
        Team.getChosenTeamColors().clear();
        Position[] spawns = {new Position(0, 0), new Position(10, 10), new Position(20, 20)};
        Team teamWith3 = new Team("Team3", 3, spawns);
        assertEquals(3, teamWith3.getNumberOfWorms());
    }

    @Test
    public void constructor_throwsExceptionForMismatchedSpawns() {
        Position[] spawns = {new Position(0, 0), new Position(10, 10)};
        assertThrows(IllegalArgumentException.class, () -> new Team("BadTeam", 3, spawns));
    }

    // ==================== Team Properties Tests ====================

    @Test
    public void getInventory_returnsNotNull() {
        assertNotNull(team.getInventory());
    }

    @Test
    public void getTeamWorms_returnsCorrectSize() {
        assertEquals(1, team.getTeamWorms().size());
    }

    @Test
    public void getNumberOfWorms_initiallyCorrect() {
        assertEquals(1, team.getNumberOfWorms());
    }

    @Test
    public void teamColor_isDifferentForMultipleTeams() {
        Team.getChosenTeamColors().clear();
        Team team1 = new Team("Team1", 1, new Position[]{new Position(0, 0)});
        Team team2 = new Team("Team2", 1, new Position[]{new Position(0, 0)});
        Team team3 = new Team("Team3", 1, new Position[]{new Position(0, 0)});
        assertNotEquals(team1.teamColor, team2.teamColor);
        assertNotEquals(team2.teamColor, team3.teamColor);
        assertNotEquals(team1.teamColor, team3.teamColor);
    }

    // ==================== Worm Management Tests ====================

    @Test
    public void getIndexWorm_retrievesCorrectWorm() {
        Worm worm = team.getIndexWorm(0);
        assertNotNull(worm);
    }

    @Test
    public void getIndexWorm_nameFollowsPattern() {
        Worm worm = team.getIndexWorm(0);
        assertEquals("TestTeam1", worm.name);
    }

    @Test
    public void getCurrentWorm_initiallyFirstWorm() {
        assertEquals(team.getIndexWorm(0), team.getCurrentWorm());
    }

    @Test
    public void addWorm_increasesWormCount() {
        int initialCount = team.getNumberOfWorms();
        Worm newWorm = new Worm("NewWorm", team);
        assertEquals(initialCount + 1, team.getNumberOfWorms());
    }

    @Test
    public void addWorm_addedWormIsInList() {
        Worm newWorm = new Worm("NewWorm", team);
        assertTrue(team.getTeamWorms().contains(newWorm));
    }

    @Test
    public void removeWorm_decreasesWormCount() {
        int initialCount = team.getNumberOfWorms();
        team.removeWorm(1);
        assertEquals(initialCount - 1, team.getNumberOfWorms());
    }

    @Test
    public void removeWorm_removesCorrectWorm() {
        Worm original = team.getIndexWorm(0);
        team.removeWorm(1);
        assertFalse(team.getTeamWorms().contains(original));
    }

    @Test
    public void removeWorm_multipleWorms() {
        Team.getChosenTeamColors().clear();
        Position[] spawns = {new Position(0, 0), new Position(10, 10)};
        Team team2 = new Team("Team2", 2, spawns);
        Worm worm1 = team2.getIndexWorm(0);
        Worm worm2 = team2.getIndexWorm(1);
        
        team2.removeWorm(1);
        assertEquals(1, team2.getNumberOfWorms());
        assertTrue(team2.getTeamWorms().contains(worm2));
        assertFalse(team2.getTeamWorms().contains(worm1));
    }

    @Test
    public void removeWorm_byHimself(){
        Worm worm2 = new Worm("DeleteWorm", team);
        assertEquals(2, team.getNumberOfWorms());
        team.removeWorm(worm2);
        assertEquals(1, team.getNumberOfWorms());
        assertFalse(team.getTeamWorms().contains(worm2));

    }

    // ==================== Worm Selection Tests ====================

    @Test
    public void nextWorm_cyclesCorrectly() {
        Worm originalWorm = team.getCurrentWorm();
        team.nextWorm();
        assertEquals(originalWorm, team.getCurrentWorm());  // Single worm wraps to itself
    }

    @Test
    public void nextWorm_multipleWorms() {
        Team.getChosenTeamColors().clear();
        Position[] spawns = {new Position(0, 0), new Position(10, 10)};
        Team team2 = new Team("Team2", 2, spawns);
        Worm worm0 = team2.getIndexWorm(0);
        Worm worm1 = team2.getIndexWorm(1);
        
        assertEquals(worm0, team2.getCurrentWorm());
        team2.nextWorm();
        assertEquals(worm1, team2.getCurrentWorm());
    }

    @Test
    public void nextWorm_wrapsAroundToFirst() {
        Team.getChosenTeamColors().clear();
        Position[] spawns = {new Position(0, 0), new Position(10, 10)};
        Team team2 = new Team("Team2", 2, spawns);
        Worm worm0 = team2.getIndexWorm(0);
        
        team2.nextWorm();  // Move to worm1
        team2.nextWorm();  // Wrap back to worm0
        assertEquals(worm0, team2.getCurrentWorm());
    }

    @Test
    public void nextWorm_threeWorms() {
        Team.getChosenTeamColors().clear();
        Position[] spawns = {new Position(0, 0), new Position(10, 10), new Position(20, 20)};
        Team team3 = new Team("Team3", 3, spawns);
        
        Worm worm0 = team3.getIndexWorm(0);
        Worm worm1 = team3.getIndexWorm(1);
        Worm worm2 = team3.getIndexWorm(2);
        
        assertEquals(worm0, team3.getCurrentWorm());
        team3.nextWorm();
        assertEquals(worm1, team3.getCurrentWorm());
        team3.nextWorm();
        assertEquals(worm2, team3.getCurrentWorm());
        team3.nextWorm();
        assertEquals(worm0, team3.getCurrentWorm());
    }

    @Test
    void testSetCurrentWormNull(){
        assertNotNull(team.getCurrentWorm());
        team.setCurrentWormNull();
        assertNull(team.getCurrentWorm());
    }

    // ==================== Weapon Management Tests ====================

    @Test
    public void isGUNUnlocked_initiallyTrue() {
        assertTrue(team.getInventory().isUnlocked(WeaponId.GUN));
    }

    @Test
    public void unlockWeapon_makesWeaponUsable() {
        team.unlockWeapon(WeaponId.BAZOOKA);
        assertTrue(team.getInventory().isUnlocked(WeaponId.BAZOOKA));
    }

    @Test
    public void lockWeapon_locksWeapon() {
        team.unlockWeapon(WeaponId.BAZOOKA);
    team.lockWeapon(WeaponId.BAZOOKA);
        assertFalse(team.getInventory().isUnlocked(WeaponId.BAZOOKA));
    }

    @Test
    public void addAmmo_addsWeaponAmmo() {
        team.addAmmo(WeaponId.BAZOOKA, 10);
        assertEquals(10, team.getInventory().weapons.getAmmo(WeaponId.BAZOOKA));
    }

    @Test
    public void addAmmo_cumulative() {
        team.addAmmo(WeaponId.BAZOOKA, 5);
        team.addAmmo(WeaponId.BAZOOKA, 5);
        assertEquals(10, team.getInventory().weapons.getAmmo(WeaponId.BAZOOKA));
    }

    // ==================== selectWeapon() Tests ====================

    @Test
    public void selectWeapon_setsHand() {
        team.selectWeapon(WeaponId.GUN);
        assertEquals(WeaponId.GUN, team.getHand());
    }

    @Test
    public void selectWeapon_throwsForUnknownWeapon() {
        assertThrows(UnknownWeaponException.class, () -> team.selectWeapon(WeaponId.NONE));
    }

    @Test
    public void selectWeapon_throwsForLockedWeapon() {
        assertThrows(LockedWeaponException.class, () -> team.selectWeapon(WeaponId.BAZOOKA));
    }

    @Test
    public void selectWeapon_throwsForWeaponWithoutAmmo() {
        team.unlockWeapon(WeaponId.BAZOOKA);
        assertThrows(UnusableWeaponException.class, () -> team.selectWeapon(WeaponId.BAZOOKA));
    }

    @Test
    public void selectWeapon_succeeds() {
        team.unlockWeapon(WeaponId.BAZOOKA);
        team.addAmmo(WeaponId.BAZOOKA, 5);
        assertDoesNotThrow(() -> team.selectWeapon(WeaponId.BAZOOKA));
    }

    @Test
    public void selectWeapon_multipleWeapons() {
        team.unlockWeapon(WeaponId.SHOTGUN);
        team.unlockWeapon(WeaponId.BAZOOKA);
        team.addAmmo(WeaponId.SHOTGUN, 5);
        team.addAmmo(WeaponId.BAZOOKA, 3);
        
        team.selectWeapon(WeaponId.SHOTGUN);
        assertEquals(WeaponId.SHOTGUN, team.getHand());
        
        team.selectWeapon(WeaponId.BAZOOKA);
        assertEquals(WeaponId.BAZOOKA, team.getHand());
    }

    // ==================== fire() Tests ====================

    @Test
    public void fire_succeeds() {
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        assertDoesNotThrow(() -> team.use(params));
    }

    @Test
    public void fire_consumesAmmo() {
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        team.use(params);
        
        assertEquals(-2, team.getInventory().weapons.getAmmo(WeaponId.GUN));
    }

    @Test
    public void fire_returnsGameCommands() {
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        var result = team.use(params);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== Tool Management Tests ====================

    @Test
    public void addAmmo_addToolAmmo() {
        team.addAmmo(ToolId.JETPACK, 5);
        assertEquals(5, team.getInventory().tools.getAmmo(ToolId.JETPACK));
    }

    @Test
    public void addAmmo_toolCumulative() {
        team.addAmmo(ToolId.JETPACK, 3);
        team.addAmmo(ToolId.JETPACK, 2);
        assertEquals(5, team.getInventory().tools.getAmmo(ToolId.JETPACK));
    }

    // ==================== selectTool() Tests ====================

    @Test
    public void selectTool_setsHand() {
        team.selectTool(ToolId.PARACHUTE);
        assertEquals(ToolId.PARACHUTE, team.getHand());
    }

    @Test
    public void selectTool_throwsForUnknownTool() {
        assertThrows(UnknownToolException.class, () -> team.selectTool(ToolId.NONE));
    }

    @Test
    public void selectTool_throwsForToolWithoutAmmo() {
        assertThrows(UnusableToolException.class, () -> team.selectTool(ToolId.JETPACK));
    }

    @Test
    public void selectTool_succeeds() {
        assertDoesNotThrow(() -> team.selectTool(ToolId.PARACHUTE));
    }

    // ==================== use() Tests ====================

    @Test
    public void use_succeeds() {
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        assertDoesNotThrow(() -> team.use(params));
    }

    @Test
    public void use_consumesAmmo() {
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        team.use(params);
        
        assertEquals(1, team.getInventory().tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void use_returnsGameCommands() {
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        var result = team.use(params);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== Integration Tests ====================

    @Test
    public void weaponAndTool_canBeSelectedIndependently() {
        
        team.selectWeapon(WeaponId.GUN);
        assertEquals(WeaponId.GUN, team.getHand());
        
        team.selectTool(ToolId.PARACHUTE);
        assertEquals(ToolId.PARACHUTE, team.getHand());
    }

    @Test
    public void getHand_initiallyNull() {
        assertNull(team.getHand());
    }

    @Test
    public void getTeamColor_returnsAssignedColor() {
        assertEquals(team.teamColor, team.getTeamColor());
    }

    @Test
    public void getCredits_initiallyZero() {
        assertEquals(0, team.getCredits());
    }

    @Test
    public void addCredits_increasesBalance() {
        team.addCredits(100);
        assertEquals(100, team.getCredits());
    }

    @Test
    public void addCredits_NegativeAmount() {
        team.addCredits(50);
        team.addCredits(-10);
        assertEquals(50, team.getCredits());
    }

    @Test
    public void removeCredits_WhenSufficientFunds() {
        team.addCredits(100);
        team.removeCredits(40);
        assertEquals(60, team.getCredits());
    }

    @Test
    public void removeCredits_WhenInsufficientFunds() {
        team.addCredits(50);
        team.removeCredits(100);
        assertEquals(50, team.getCredits());
    }

    @Test
    public void removeCredits_WhenNegativeAmount() {
        team.addCredits(100);
        team.removeCredits(-20);
        assertEquals(100, team.getCredits());
    }
}
