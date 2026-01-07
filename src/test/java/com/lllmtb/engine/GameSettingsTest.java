package com.lllmtb.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.ConfigMap;
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
        ConfigMap configMap = new ConfigMap(100, 0,0, GeneratedIsland,0, terrainType, null);
        GameSettings settings = new GameSettings("blabla", nbTeams, TeamNames, numberWorms,100, 10000, hasWind, FileMapName, configMap, 2, GameMode.NORMAL, true, false);
        assertEquals(nbTeams, settings.nbTeams);
        assertEquals(TeamNames, settings.teamNames);
        assertEquals(numberWorms, settings.nbWorms);
        assertEquals(hasWind, settings.hasWind);
        assertEquals(FileMapName, settings.mapFileName);
        assertEquals(configMap, settings.configMap);
    }

    @Test
    public void testNullGameNameThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings(null, 2, teamNames, 3, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testEmptyGameNameThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("   ", 2, teamNames, 3, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testLessThanOneTeamThrowsException() {
        String[] teamNames = {};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 0, teamNames, 3, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testZeroWormsThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 0, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNullTeamNamesThrowsException() {
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, null, 3, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testInsufficientTeamNamesThrowsException() {
        String[] teamNames = {"Team1"}; // Only 1 name but 2 teams required
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNegativeMaxHpThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, -50, 60, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testZeroTurnDurationThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(IllegalArgumentException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 0, false, null, configMap, 100, GameMode.NORMAL, false, false));
    }

    @Test
    public void testNullGameModeThrowsException() {
        String[] teamNames = {"Team1", "Team2"};
        ConfigMap configMap = new ConfigMap(100, 0,0, true,0, LandScape.HILLY, null);
        assertThrows(NullPointerException.class, () -> new GameSettings("TestGame", 2, teamNames, 3, 100, 60, false, null, configMap, 2, null, false, false));
    }
}
