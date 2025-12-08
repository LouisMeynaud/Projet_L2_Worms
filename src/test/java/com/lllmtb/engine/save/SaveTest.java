package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.Save.Save;
import com.lllmtb.events.EventBus;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveTest {
    private static GameWorld gameWorld;
    private static EventBus eventBus;
    private static GameSettings settings;

    @BeforeAll
    static void setUp() {
    eventBus = new EventBus();
    settings = new GameSettings(2, new String[]{"test","test2"}, 2, 10,true, "testGame", true, 200 , LandScape.FLAT);
    gameWorld = new GameWorld(settings, eventBus);
    Save.setGameWorld(gameWorld);
    }

    @Test
    void saveAllTest() throws IOException {
    Save.saveAll();
    Path path = Paths.get("src/test/resources/SaveTest/testGame");
    assertTrue(Files.exists(path));
    assertTrue(Files.exists(path.resolve("hightline.txt")));
    assertTrue(Files.exists(path.resolve("map.txt")));
    assertTrue(Files.exists(path.resolve("test.json")));
    }

    @Test
    void saveGameWorldTest() throws IOException {
    Save.saveGameWorld(gameWorld, "src/test/resources/SaveTest/testGame");
    Path path = Paths.get("src/test/resources/SaveTest/testGame");
    assertTrue(Files.exists(path));
    assertTrue(Files.exists(path.resolve("test.json")));
    }

    @Test
    void saveMapTest() throws IOException {
        Save.saveMap(gameWorld.getMap(), "src/test/resources/SaveTest/testGame");
        Path path = Paths.get("src/test/resources/SaveTest/testGame");
        assertTrue(Files.exists(path));
        assertTrue(Files.exists(path.resolve("map.txt")));
    }
    @Test
    void saveHightlineTest() throws IOException {
        Save.saveHightline(gameWorld.getMap(), "src/test/resources/SaveTest/testGame");
        Path path = Paths.get("src/test/resources/SaveTest/testGame");
        assertTrue(Files.exists(path));
        assertTrue(Files.exists(path.resolve("hightline.txt")));
    }

    @Test
    void saveGameWorldExeption() throws IOException {}

}
