package com.lllmtb.Engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameSettingsTest
{
    @Test
    public void ConstructorTest() {
        int nbTeams = 2;
        String[] TeamNames = {"Java", "Python"};
        int numberWorms =4;
        boolean isWind = true;
        String FileMapPath = "tkt.txt";
        boolean GeneratedIsland = true;
        GameSettings settings = new GameSettings(nbTeams, TeamNames, numberWorms, isWind, FileMapPath, GeneratedIsland);
        assertEquals(nbTeams, settings.nbTeams);
        assertEquals(TeamNames, settings.TeamNames);
        assertEquals(numberWorms, settings.numberWorms);
        assertEquals(isWind, settings.isWind);
        assertEquals(FileMapPath, settings.mapFilePath);
        assertEquals(GeneratedIsland, settings.GenerateIsland);
    }
}
