package com.lllmtb.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;
public class GameSettingsTest
{
    @Test
    public void ConstructorTest() {
        int nbTeams = 2;
        String[] TeamNames = {"Java", "Python"};
        int numberWorms =4;
        boolean hasWind = true;
        String FileMapName = "tkt.txt";
        boolean GeneratedIsland = true;
        LandScape terrainType = LandScape.HILLY;
        GameSettings settings = new GameSettings("blabla", nbTeams, TeamNames, numberWorms,100, 10000, hasWind, FileMapName, GeneratedIsland,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
        assertEquals(nbTeams, settings.nbTeams);
        assertEquals(TeamNames, settings.teamNames);
        assertEquals(numberWorms, settings.nbWorms);
        assertEquals(hasWind, settings.hasWind);
        assertEquals(FileMapName, settings.mapFileName);
        assertEquals(GeneratedIsland, settings.floatingIsland);
        assertEquals(terrainType,settings.terrainType);
    }

    @Test
    public void testNullGameNameThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings(null, 2, teamNames, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testEmptyGameNameThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("   ", 2, teamNames, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testLessThanOneTeamThrowsException() {
        String[] teamNames = {};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 0, teamNames, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testZeroWormsThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 0, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNullTeamNamesThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, null, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testInsufficientTeamNamesThrowsException() {
        String[] teamNames = {"Team1"}; // Only 1 name but 2 teams required
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNegativeMaxHpThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, -50, 60, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testZeroTurnDurationThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 0, false, null, false, 100, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testTooSmallMapWidthThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, false, 30, LandScape.HILLY, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNullTerrainTypeThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(NullPointerException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, false, 100, null, 2, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNullGameModeThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        assertThrows(NullPointerException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, false, 100, LandScape.HILLY, 2, null, false, false));
    }
}
