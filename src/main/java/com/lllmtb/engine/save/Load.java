package com.lllmtb.engine.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.eclipse.jdt.annotation.NonNull;

import java.io.FileNotFoundException;
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
     * @throws FileNotFoundException If the JSON file does not exist in the expected directory.
     * @throws RuntimeException If an I/O error occurs during reading.
     */
    public static GameSave loadGameSave(String name) throws FileNotFoundException {
        String stringpath = SaveConfig.SAVE_DIRECTORY + name + "/save.json";
        if (name.contains("Test")) {
            stringpath = "src/test/resources/saveTest/" + name + "/save.json";
        }
        RuntimeTypeAdapterFactory<Entity> entityAdapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Projectile.class, "projectile")
                .registerSubtype(Worm.class, "worm")
                .registerSubtype(Coin.class, "coin")
                .registerSubtype(Drop.class, "drop");


        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(entityAdapter)
                .create();

        try (FileReader reader = new FileReader(stringpath)) {
            GameSave save = gson.fromJson(reader, GameSave.class);
            return save;
    } catch (IOException e) {
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
        for (PhysicsBodySave saveBody : gameSave.entities) {
            try {
            if (saveBody.entity instanceof Worm worm) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                    Field bodyWormField = Worm.class.getDeclaredField("body");
                    bodyWormField.setAccessible(true);
                    bodyWormField.set(worm, saveBody.physicsBody);
            } else if (saveBody.entity instanceof Projectile projectile) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                Field bodyProjectileField = Projectile.class.getDeclaredField("body");
                bodyProjectileField.setAccessible(true);
                bodyProjectileField.set(projectile, saveBody.physicsBody);

                // use reflexion to recreate worm instance with correct PhysicsBody
                Field entityProjectileField = PhysicsBody.class.getDeclaredField("entity");
                entityProjectileField.setAccessible(true);
                entityProjectileField.set(saveBody.physicsBody, projectile);


            } else if (saveBody.entity instanceof Coin coin) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                System.out.println("chargement coin");
                Field bodyCoinField = Coin.class.getDeclaredField("body");
                bodyCoinField.setAccessible(true);
                bodyCoinField.set(coin, saveBody.physicsBody);

                // use reflexion to recreate worm instance with correct PhysicsBody
                Field entityCoinField = PhysicsBody.class.getDeclaredField("entity");
                entityCoinField.setAccessible(true);
                entityCoinField.set(saveBody.physicsBody, coin);

            }else if (saveBody.entity instanceof Drop drop) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                System.out.println("chargement drop");
                Field bodyDropField = Drop.class.getDeclaredField("body");
                bodyDropField.setAccessible(true);
                bodyDropField.set(drop, saveBody.physicsBody);

                // use reflexion to recreate worm instance with correct PhysicsBody
                Field entityDropField = PhysicsBody.class.getDeclaredField("entity");
                entityDropField.setAccessible(true);
                entityDropField.set(saveBody.physicsBody, drop);
            }


            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        for (PhysicsBodySave savePhysicsBody : gameSave.entities) {
            try {
                Field entityField = PhysicsBody.class.getDeclaredField("entity");
                entityField.setAccessible(true);
                entityField.set(savePhysicsBody.physicsBody, savePhysicsBody.entity);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        for (TeamSave teamSave: gameSave.teams) {
            for (Worm worm : teamSave.team.getTeamWorms()) {
                if (worm.uuid == teamSave.team.getCurrentWorm().uuid) {
                    try {
                        Field currentWormField = Team.class.getDeclaredField("currentWorm");
                        currentWormField.setAccessible(true);
                        currentWormField.set(teamSave.team, worm);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
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
        try {
            GameSave gameSave = loadGameSave(name);
            reforgeGameSave(gameSave);
            Map map = MapLoader.loadMatrixLoad(name);
            return new GameWorld(gameSave, map);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
