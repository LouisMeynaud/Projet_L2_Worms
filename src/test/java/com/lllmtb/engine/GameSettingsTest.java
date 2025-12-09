package com.lllmtb.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        GameSettings settings = new GameSettings(nbTeams, TeamNames, numberWorms,100, 10000, hasWind, FileMapName, GeneratedIsland,370,LandScape.HILLY,2);
        assertEquals(nbTeams, settings.nbTeams);
        assertEquals(TeamNames, settings.teamNames);
        assertEquals(numberWorms, settings.nbWorms);
        assertEquals(hasWind, settings.hasWind);
        assertEquals(FileMapName, settings.mapFileName);
        assertEquals(GeneratedIsland, settings.floatingIsland);
        assertEquals(terrainType,settings.terrainType);
    }
}
