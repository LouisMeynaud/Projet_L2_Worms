package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.lllmtb.engine.save.Load.reforgeGameSave;
import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

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

  /*  @AfterAll
    static void tearDown() throws IOException {
       Path path = Paths.get("src/test/resources/savesTest/");
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
    }*/

    @Test
    void loadGameSaveTest() {
        GameSave gameSave = Load.loadGameSave("Test");

        assertNotNull(gameSave);
        assertFalse(gameSave.teams.isEmpty());
        assertFalse(gameSave.entities.isEmpty());
    }

    @Test
    void reforgeGameSaveTest() {
        Position position = new Position(2,3);
        Team team = new Team("test",1, new Position[]{position}, 150 );
        Worm worm1 = new Worm("cc",team);
        GameSave gameSave = Load.loadGameSave("Test");

        Coin coin = new Coin(position, 23);
        PhysicsBodySave coinSave = new PhysicsBodySave(coin.getBody());

        Drop drop  = new Drop(position, WeaponId.BAZOOKA);
        PhysicsBodySave dropSave = new PhysicsBodySave(drop.getBody());

        UseParams useParams = new UseParams(worm1);
        Projectile projectile = new BulletProjectile(useParams);
        PhysicsBodySave projectileSave = new PhysicsBodySave(projectile.getBody());

        gameSave.entities.add(coinSave);
        gameSave.entities.add(dropSave);
        gameSave.entities.add(projectileSave);
        reforgeGameSave(gameSave);

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