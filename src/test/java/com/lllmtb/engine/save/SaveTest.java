package com.lllmtb.engine.save;

import org.junit.jupiter.api.BeforeAll;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;

public class SaveTest {
    private static GameWorld gameWorld;
    private static EventBus eventBus;
    private static GameSettings settings;

    @BeforeAll
    static void setUp() {
    eventBus = new EventBus();
    settings = new GameSettings("blabla", 2, new String[]{"test","test2"}, new boolean[2], 2, 100, 10,true, "testGame", true, 200, LandScape.FLAT, 2,GameMode.NORMAL, true, false);
    gameWorld = new GameWorld(settings, eventBus);
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
