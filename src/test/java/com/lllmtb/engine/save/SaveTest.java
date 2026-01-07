package com.lllmtb.engine.save;

import org.junit.jupiter.api.BeforeAll;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;

public class SaveTest {

    @BeforeAll
    static void setUp() {
        EventBus eventBus = new EventBus();
        ConfigMap configMap = new ConfigMap(100, 0,0, false,0, LandScape.FLAT, null);
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"test", "test2"}, 2, 100, 10, true, "testGame", configMap, 2, GameMode.NORMAL, true, false);
        GameWorld gameWorld = new GameWorld(settings, eventBus);
    Save.setGameWorld(gameWorld);
    }
/*
    @Test
    void saveAllTest() throws IOException {
    Save.saveAll();
    Path path = Paths.get("src/test/resources/SaveTest/testGame");
    assertTrue(Files.exists(path));
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
    void saveGameWorldExeption() throws IOException {}
*/
}
