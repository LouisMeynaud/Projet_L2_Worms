package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {
    private static final String TEST_PATH = "src/test/resources/savesTest/Test" ;

    @BeforeAll
    static void setUp() throws IOException {
        EventBus eventBus = new EventBus();
        GameSettings settings = new GameSettings(
                "Test", 2, new String[]{"Team1", "Team2"}, 2, 100, 10,
                true, "test", true, 200, LandScape.FLAT, 2,
                GameMode.NORMAL, true, false
        );
        GameWorld world = new GameWorld(settings, eventBus);
        Save.setGameWorld(world);
        Save.saveAll();
    }

    @AfterAll
    static void tearDown() throws IOException {
        Path path = Paths.get(TEST_PATH);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ignored) {}
                    });
            Files.deleteIfExists(path);
        }
        Save.setGameWorld(null);
    }

    @Test
    void loadGameSaveTest() {
        GameSave gameSave = Load.loadGameSave("Test");

        assertNotNull(gameSave);
        assertFalse(gameSave.teams.isEmpty());
        assertFalse(gameSave.entities.isEmpty());
    }

    @Test
    void reforgeGameSaveTest() {
        GameSave gameSave = Load.loadGameSave("Test");
        Load.reforgeGameSave(gameSave);

        for (PhysicsBodySave saveBody : gameSave.entities) {
            assertNotNull(saveBody.entity);

            if (saveBody.entity instanceof Worm worm) {
                assertNotNull(worm.getBody());
            }
        }
    }

    @Test
    void loadAllTest() {
        GameWorld world = Load.loadAll("Test");
        assertNotNull(world);
        assertNotNull(world.getMap());
        assertFalse(world.getTeams().isEmpty());
    }

    @Test
    void recoverItemTest() {
        assertInstanceOf(WeaponId.class, Load.recoverItem("WEAPONID", "BAZOOKA"));
        assertInstanceOf(ToolId.class, Load.recoverItem("TOOLID", "JETPACK"));
    }

    @Test
    void getListeNomsLoadsTest() {
        String[] saves = Load.getListeNomsLoads();
        assertNotNull(saves);
    }

    @Test
    void loadMissingFileExceptionTest() {
        assertThrows(RuntimeException.class, () -> Load.loadGameSave("NonExistentSave"));
    }
}