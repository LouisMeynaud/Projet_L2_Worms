package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameSaveTest {
    public static GameWorld world;
    @BeforeAll
    static void setUp() throws IOException {
        EventBus eventBus = new EventBus();
        ConfigMap configMap = new ConfigMap(100, 0, 0, false, 0, LandScape.FLAT, null);
        GameSettings settings = new GameSettings(
                "Test", 2, new String[]{"Team1", "Team2"}, new boolean[]{false,true}, 2, 100, 10,
                true, "test", configMap, 2,
                GameMode.NORMAL, true, false
        );
        world = new GameWorld(settings, eventBus);
    }

    @Test
    void constructorTest() {
        GameSave gameSave = new GameSave(world);
        assertNotNull(gameSave);
        assertEquals(world.getGameName(), gameSave.gameSettings.gameName);
    }
    @Test
    void toStringTest() {
        GameSave gameSave = new GameSave(world);
        assertNotNull(gameSave.toString());
    }

}
