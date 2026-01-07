package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
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
        GameSettings settings = new GameSettings(
                "Test", 2, new String[]{"Team1", "Team2"}, 2, 100, 10,
                true, "test", true, 200, LandScape.FLAT, 2,
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
