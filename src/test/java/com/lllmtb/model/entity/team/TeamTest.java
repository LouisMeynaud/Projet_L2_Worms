package com.lllmtb.model.entity.team;

import com.lllmtb.engine.GameEngine;
import com.lllmtb.engine.save.GameSave;
import com.lllmtb.engine.save.TeamSave;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.weapon.UnknownWeaponException;
import com.lllmtb.model.item.weapon.LockedWeaponException;
import com.lllmtb.model.item.weapon.UnusableWeaponException;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.tool.UnknownToolException;
import com.lllmtb.model.item.tool.UnusableToolException;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    public void constructor_withTeamSave() {
            TeamSave teamSave = new TeamSave(team);
            Team team2 = new Team(teamSave);
            assertNotNull(team2.id);
            assertEquals(team.id, team2.id);
            assertEquals(team.name, team2.name);
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

    // ==================== Weapon Management Tests ====================

    @Test
    public void unlockWeapon_makesWeaponUsable() {
        team.unlockWeapon(WeaponId.GUN);
        assertTrue(team.getInventory().isUnlocked(WeaponId.GUN));
    }

    @Test
    public void lockWeapon_locksWeapon() {
        team.unlockWeapon(WeaponId.GUN);
        team.lockWeapon(WeaponId.GUN);
        assertFalse(team.getInventory().isUnlocked(WeaponId.GUN));
    }

    @Test
    public void addAmmo_addsWeaponAmmo() {
        team.addAmmo(WeaponId.GUN, 10);
        assertEquals(10, team.getInventory().weapons.getAmmo(WeaponId.GUN));
    }

    @Test
    public void addAmmo_cumulative() {
        team.addAmmo(WeaponId.GUN, 5);
        team.addAmmo(WeaponId.GUN, 5);
        assertEquals(10, team.getInventory().weapons.getAmmo(WeaponId.GUN));
    }

    // ==================== selectWeapon() Tests ====================

    @Test
    public void selectWeapon_setsHand() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 1);
        team.selectWeapon(WeaponId.GUN);
        assertEquals(WeaponId.GUN, team.getHand());
    }

    @Test
    public void selectWeapon_throwsForUnknownWeapon() {
        assertThrows(UnknownWeaponException.class, () -> team.selectWeapon(WeaponId.NONE));
    }

    @Test
    public void selectWeapon_throwsForLockedWeapon() {
        assertThrows(LockedWeaponException.class, () -> team.selectWeapon(WeaponId.GUN));
    }

    @Test
    public void selectWeapon_throwsForWeaponWithoutAmmo() {
        team.unlockWeapon(WeaponId.GUN);
        assertThrows(UnusableWeaponException.class, () -> team.selectWeapon(WeaponId.GUN));
    }

    @Test
    public void selectWeapon_succeeds() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 5);
        assertDoesNotThrow(() -> team.selectWeapon(WeaponId.GUN));
    }

    @Test
    public void selectWeapon_multipleWeapons() {
        team.unlockWeapon(WeaponId.GUN);
        team.unlockWeapon(WeaponId.BAZOOKA);
        team.addAmmo(WeaponId.GUN, 5);
        team.addAmmo(WeaponId.BAZOOKA, 3);
        
        team.selectWeapon(WeaponId.GUN);
        assertEquals(WeaponId.GUN, team.getHand());
        
        team.selectWeapon(WeaponId.BAZOOKA);
        assertEquals(WeaponId.BAZOOKA, team.getHand());
    }

    // ==================== fire() Tests ====================

    @Test
    public void fire_succeeds() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 10);
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        assertDoesNotThrow(() -> team.use(params));
    }

    @Test
    public void fire_consumesAmmo() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 10);
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        team.use(params);
        
        assertEquals(9, team.getInventory().weapons.getAmmo(WeaponId.GUN));
    }

    @Test
    public void fire_returnsGameCommands() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 10);
        team.selectWeapon(WeaponId.GUN);
        
        UseParams params = new UseParams(0, new Position(0, 0), team.getCurrentWorm());
        var result = team.use(params);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== Tool Management Tests ====================

    @Test
    public void addAmmo_addToolAmmo() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        assertEquals(5, team.getInventory().tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void addAmmo_toolCumulative() {
        team.addAmmo(ToolId.PARACHUTE, 3);
        team.addAmmo(ToolId.PARACHUTE, 2);
        assertEquals(5, team.getInventory().tools.getAmmo(ToolId.PARACHUTE));
    }

    // ==================== selectTool() Tests ====================

    @Test
    public void selectTool_setsHand() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        team.selectTool(ToolId.PARACHUTE);
        assertEquals(ToolId.PARACHUTE, team.getHand());
    }

    @Test
    public void selectTool_throwsForUnknownTool() {
        assertThrows(UnknownToolException.class, () -> team.selectTool(ToolId.NONE));
    }

    @Test
    public void selectTool_throwsForToolWithoutAmmo() {
        assertThrows(UnusableToolException.class, () -> team.selectTool(ToolId.PARACHUTE));
    }

    @Test
    public void selectTool_succeeds() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        assertDoesNotThrow(() -> team.selectTool(ToolId.PARACHUTE));
    }

    // ==================== use() Tests ====================

    @Test
    public void use_succeeds() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        assertDoesNotThrow(() -> team.use(params));
    }

    @Test
    public void use_consumesAmmo() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        team.use(params);
        
        assertEquals(4, team.getInventory().tools.getAmmo(ToolId.PARACHUTE));
    }

    @Test
    public void use_returnsGameCommands() {
        team.addAmmo(ToolId.PARACHUTE, 5);
        team.selectTool(ToolId.PARACHUTE);
        
        UseParams params = new UseParams(team.getCurrentWorm());
        var result = team.use(params);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ==================== Integration Tests ====================

    @Test
    public void weaponAndTool_canBeSelectedIndependently() {
        team.unlockWeapon(WeaponId.GUN);
        team.addAmmo(WeaponId.GUN, 10);
        team.addAmmo(ToolId.PARACHUTE, 5);
        
        team.selectWeapon(WeaponId.GUN);
        assertEquals(WeaponId.GUN, team.getHand());
        
        team.selectTool(ToolId.PARACHUTE);
        assertEquals(ToolId.PARACHUTE, team.getHand());
    }

    @Test
    public void getHand_initiallyNull() {
        assertNull(team.getHand());
    }
}
