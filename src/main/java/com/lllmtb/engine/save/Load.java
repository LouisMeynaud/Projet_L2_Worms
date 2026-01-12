package com.lllmtb.engine.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.effect.BurnEffect;
import com.lllmtb.model.entity.effect.PoisonEffect;
import com.lllmtb.model.entity.effect.StatusEffect;
import com.lllmtb.model.entity.projectile.MineProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.projectile.TowerProjectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.eclipse.jdt.annotation.NonNull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;


/**
 * Utility class responsible for loading game data from the file system.
 * <p>
 * This class handles the deserialization of the {@link GameSave} object from JSON
 * and reconstructs complex object relationships (such as PhysicsBodies and Team references)
 * that may have been severed or handled specifically during the serialization process.
 * </p>
 */
public class Load {

    /**
     * Deserializes the {@link GameSave} object from a JSON file.
     * <p>
     * This method configures GSON with a {@link RuntimeTypeAdapterFactory} to correctly
     * instantiate polymorphic subtypes of {@link Entity} (specifically {@link Projectile} and {@link Worm}).
     * It reads from the hardcoded path {@code src/test/resources/SaveTest/[name]/test.json}.
     * </p>
     *
     * @param name The name of the save folder to load from.
     * @return The raw deserialized {@link GameSave} object.
     * @throws RuntimeException If an I/O error occurs during reading.
     */
    public static GameSave loadGameSave(String name) {
        String stringpath = SaveConfig.SAVE_DIRECTORY + name + "/save.json";
        if (name.contains("Test")) {
            stringpath = "src/test/resources/savesTest/" + name + "/save.json";
        }
        RuntimeTypeAdapterFactory<Entity> entityAdapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Projectile.class, "projectile")
                .registerSubtype(Worm.class, "worm")
                .registerSubtype(Coin.class, "coin")
                .registerSubtype(Drop.class, "drop")
                .registerSubtype(TowerProjectile.class, "tower")
                .registerSubtype(MineProjectile.class, "mine");

        RuntimeTypeAdapterFactory<StatusEffect> effectAdapter = RuntimeTypeAdapterFactory.of(StatusEffect.class, "type")
                .registerSubtype(BurnEffect.class, "burnEffect")
                .registerSubtype(PoisonEffect.class, "poisonEffect");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapterFactory(entityAdapter)
                .registerTypeAdapterFactory(effectAdapter)
                .create();

        try (FileReader reader = new FileReader(stringpath)) {
            return gson.fromJson(reader, GameSave.class);
    } catch (IOException e) {
            Logger.logger.error("Error with while reading the file {} : {}", stringpath, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Reconstructs the runtime links between entities, physics bodies, and teams using Reflection.
     * <p>
     * JSON deserialization often creates detached objects. This method iterates through the
     * loaded entities and performs the following repair operations:
     * <ul>
     * <li>Matches {@link Worm} entities to their {@link PhysicsBodySave} using UUIDs.</li>
     * <li>Re-injects the correct {@link com.lllmtb.model.entity.team.Team} reference into {@link Worm} instances.</li>
     * <li>Re-injects the physical body reference into {@link Worm} and {@link Projectile} instances.</li>
     * </ul>
     * This is necessary because fields like {@code body} or {@code team} might be transient or circular.
     * </p>
     *
     * @param gameSave The {@link GameSave} object containing the detached entities to be reforged.
     * @throws RuntimeException If a reflection error (NoSuchFieldException, IllegalAccessException) occurs.
     */
    public static void reforgeGameSave(@NonNull GameSave gameSave) {
        if (gameSave.teams.get(gameSave.indexTeam).team.getCurrentWorm() == null) {
            gameSave.teams.get(gameSave.indexTeam).team.nextWorm();
        }
        for (PhysicsBodySave saveBody : gameSave.entities) {
            for (TeamSave teamSave : gameSave.teams) {
                for (Worm worm : teamSave.team.getTeamWorms()) {
                    if (worm.uuid == saveBody.uuid) {
                            saveBody.physicsBody =  worm.getBody();
                            saveBody.entity = worm;
                    }
                }
            }
        }
        try {
        for (PhysicsBodySave saveBody : gameSave.entities) {
            if (saveBody.entity instanceof Worm worm) {
                // use reflexion
                    Field bodyWormField = Worm.class.getDeclaredField("body");
                    bodyWormField.setAccessible(true);
                    bodyWormField.set(worm, saveBody.physicsBody);

                // use reflexion
                Field entityWormField = PhysicsBody.class.getDeclaredField("entity");
                entityWormField.setAccessible(true);
                entityWormField.set(saveBody.physicsBody, worm);


            } else if (saveBody.entity instanceof Projectile projectile) {
                // use reflexion
                Field bodyProjectileField = Projectile.class.getDeclaredField("body");
                bodyProjectileField.setAccessible(true);
                bodyProjectileField.set(projectile, saveBody.physicsBody);

                // use reflexion
                Field entityProjectileField = PhysicsBody.class.getDeclaredField("entity");
                entityProjectileField.setAccessible(true);
                entityProjectileField.set(saveBody.physicsBody, projectile);


            } else if (saveBody.entity instanceof Coin coin) {
                // use reflexion
                Field bodyCoinField = Coin.class.getDeclaredField("body");
                bodyCoinField.setAccessible(true);
                bodyCoinField.set(coin, saveBody.physicsBody);

                // use reflexion
                Field entityCoinField = PhysicsBody.class.getDeclaredField("entity");
                entityCoinField.setAccessible(true);
                entityCoinField.set(saveBody.physicsBody, coin);

            } else if (saveBody.entity instanceof Drop drop) {
                // use reflexion
                Field bodyDropField = Drop.class.getDeclaredField("body");
                bodyDropField.setAccessible(true);
                bodyDropField.set(drop, saveBody.physicsBody);

                // use reflexion
                Field entityDropField = PhysicsBody.class.getDeclaredField("entity");
                entityDropField.setAccessible(true);
                entityDropField.set(saveBody.physicsBody, drop);

                // use reflexion
                Field itemIdField = Drop.class.getDeclaredField("itemIdToUnlock");
                itemIdField.setAccessible(true);
                itemIdField.set(drop, Load.recoverItem(drop.getItemCategory(), drop.getItemName()));
            }
        }
        for (PhysicsBodySave savePhysicsBody : gameSave.entities) {
                Field entityField = PhysicsBody.class.getDeclaredField("entity");
                entityField.setAccessible(true);
                entityField.set(savePhysicsBody.physicsBody, savePhysicsBody.entity);
        }
        for (TeamSave teamSave: gameSave.teams) {
            for (Worm worm : teamSave.team.getTeamWorms()) {
                if (worm.uuid == teamSave.team.getCurrentWorm().uuid) {
                        Field currentWormField = Team.class.getDeclaredField("currentWorm");
                        currentWormField.setAccessible(true);
                        currentWormField.set(teamSave.team, worm);
                }
            }
        }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Logger.logger.error("Problem while rewriting the gameSave: {}", e.getMessage());
        }
    }



    /**
     * Orchestrates the complete loading process to create a playable {@link GameWorld}.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Deserializes the {@link GameSave} from JSON.</li>
     * <li>Reforges the internal object links (Reflection).</li>
     * <li>Loads the terrain {@link Map} using {@link MapLoader}.</li>
     * <li>Constructs and returns the final {@link GameWorld}.</li>
     * </ol>
     * </p>
     *
     * @param name The name of the save (folder) to load.
     * @return A fully initialized {@link GameWorld} ready for the game loop.
     * @throws RuntimeException If the save file is not found or other loading errors occur.
     */
    public  static GameWorld loadAll(String name) {
        GameSave gameSave = loadGameSave(name);
        reforgeGameSave(gameSave);
        Map map = MapLoader.loadMapLoad(name);
        return new GameWorld(gameSave, map);
    }


    /**
     * Retrieves an item identifier (ItemId) based on its category and name.
     * <p>
     * This method converts strings read from the save file into actual
     * enumeration constants: {@link WeaponId} or {@link ToolId}.
     * </p>
     *
     * @param category The category of the item (must be "WEAPONID" for a weapon,
     * otherwise it is treated as a tool).
     * @param name     The technical name of the item (e.g., "BAZOOKA").
     * @return The corresponding instance of {@link ItemId}.
     * @throws IllegalArgumentException If the name does not match any constant
     * in the target enumeration.
     */
    public static ItemId recoverItem(String category, String name) {
        if ("WEAPONID".equals(category)) {
            return WeaponId.valueOf(name);
        } else {
            return ToolId.valueOf(name);
        }
    }

    /**
     * Scans the save directory to list the names of all available save games.
     * <p>
     * This method checks for the existence of the root directory defined in
     * {@link SaveConfig#SAVE_DIRECTORY} and filters only subdirectories
     * (each subdirectory representing a save).
     * </p>
     *
     * @return An array of strings containing the names of the save folders.
     * Returns an empty array if the directory is invalid, non-existent, or empty.
     */
    public static String[] getListeNomsLoads() {
        File folder = new File(SaveConfig.SAVE_DIRECTORY);
        if (!folder.exists() || !folder.isDirectory()) {
            return new String[0];
        }
        File[] subFolders = folder.listFiles(File::isDirectory);
        if (subFolders == null) {
            return new String[0];
        }
        String[] saveNames = new String[subFolders.length];
        for (int i = 0; i < subFolders.length; i++) {
            saveNames[i] = subFolders[i].getName();
        }
        return saveNames;
    }
}
