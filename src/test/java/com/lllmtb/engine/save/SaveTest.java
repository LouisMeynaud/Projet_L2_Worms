package com.lllmtb.engine.save;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsEngine;
import org.junit.jupiter.api.*;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;
import org.mockito.MockedStatic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.lllmtb.engine.save.Save.eraseSave;
import static com.lllmtb.engine.save.Save.gameWorld;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SaveTest {

    private static final String TEST_BASE_PATH = "src/test/resources/savesTest/Test" ;

    @BeforeAll
    static void setUp() {
        EventBus eventBus = new EventBus();

        ConfigMap configMap = new ConfigMap(100, 0, 0, false, 0, LandScape.FLAT, null);
        GameSettings settings = new GameSettings("Test", 2, new String[]{"test", "test2"}, new boolean[]{false, false}, 2, 100, 10, true, "test", configMap, 2, GameMode.NORMAL, true, false);
        GameWorld gameWorld = new GameWorld(settings, eventBus);
        Save.setGameWorld(gameWorld);
    }


        @Test
        void saveAllTest() {
            Save.saveAll();
            Path path = Paths.get(TEST_BASE_PATH);
            assertTrue(Files.exists(path));
            assertTrue(Files.exists(path.resolve("map.txt")));
            assertTrue(Files.exists(path.resolve("save.json")));
            assertTrue(Files.exists(path.resolve("info.txt")));
            assertTrue(Files.exists(path.resolve("image.png")));
        }

        @Test
        void saveGameWorldTest() throws IOException {
            Save.saveGameWorld(gameWorld, TEST_BASE_PATH);
            Path path = Paths.get(TEST_BASE_PATH, "save.json");
            assertTrue(Files.exists(path));
        }

        @Test
        void saveMapTest() throws IOException {
            Save.saveMap(gameWorld.getMap(), TEST_BASE_PATH);
            Path path = Paths.get(TEST_BASE_PATH, "map.txt");
            assertTrue(Files.exists(path));
        }

        @Test
        void saveInfoTest() {
            Save.saveInfo(gameWorld, TEST_BASE_PATH);
            Path path = Paths.get(TEST_BASE_PATH, "info.txt");
            assertTrue(Files.exists(path));
        }

        @Test
        void saveImageTest() throws IOException {
            BufferedImage image = Save.maptoBuffered(gameWorld.getMap());
            assertNotNull(image);
            Save.saveImage(image, TEST_BASE_PATH);
            Path path = Paths.get(TEST_BASE_PATH, "image.png");
            assertTrue(Files.exists(path));
        }
        @Test
        void eraseSaveTest() {
            assertDoesNotThrow(()->eraseSave("Id1789876573928"));
            EventBus eventBus = new EventBus();
            ConfigMap configMap = new ConfigMap(100, 0, 0, false, 0, LandScape.FLAT, null);
            GameSettings settings = new GameSettings("Id1789876573928", 2, new String[]{"test", "test2"}, new boolean[]{false, false}, 2, 100, 10, true, "test", configMap, 2, GameMode.NORMAL, true, false);
            GameWorld gameWorld = new GameWorld(settings, eventBus);
            Save.setGameWorld(gameWorld);
            Save.saveAll();
            Path id1789876573928 = Paths.get(SaveConfig.SAVE_DIRECTORY, "Id1789876573928");
            assertTrue(Files.exists(id1789876573928));
            eraseSave("Id1789876573928");
            assertFalse(Files.exists(id1789876573928));

        }
}
