package com.lllmtb.engine.save;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.lllmtb.engine.save.Save.eraseSave;
import static com.lllmtb.engine.save.Save.gameWorld;
import static org.junit.jupiter.api.Assertions.*;

public class SaveTest {

    private static final String TEST_BASE_PATH = "src/test/resources/savesTest/Test" ;

    @BeforeAll
    static void setUp() {
        EventBus eventBus = new EventBus();
        GameSettings settings = new GameSettings("Test" , 2, new String[]{"test", "test2"}, 2, 100, 10, true, "test", true, 200, LandScape.FLAT, 2, GameMode.NORMAL, true, false);
        GameWorld gameWorld = new GameWorld(settings, eventBus);
        Save.setGameWorld(gameWorld);
    }

  /*  @AfterAll
    static void tearDown() throws IOException {
        Path path = Paths.get(TEST_BASE_PATH);
        if (Files.exists(path)) {
            Files.walk(path)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ignored) {}
                    });
            Files.deleteIfExists(path);
        }

        Save.setGameWorld(null);
    }*/

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
        void eraseSaveTest() throws IOException {
            String folderToErase = "DeleteMe";
            Path path = Paths.get(SaveConfig.SAVE_DIRECTORY, folderToErase);
            Files.createDirectories(path);
            Files.writeString(path.resolve("temp.txt"), "data");
            assertTrue(Files.exists(path));
            Save.eraseSave(folderToErase);
            assertFalse(Files.exists(path), "Comment ca il a pas disparu ????");

        }
}
