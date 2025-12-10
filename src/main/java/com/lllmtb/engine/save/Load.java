package com.lllmtb.engine.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import org.eclipse.jdt.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

public class Load {
    public static GameSave loadGameSave(String name) throws FileNotFoundException {
        String stringpath = "src/test/resources/SaveTest/" + name + "/test.json";
        RuntimeTypeAdapterFactory<Entity> entityAdapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Projectile.class, "projectile") // Si le JSON contient "type": "bazooka", on crée un Bazooka
                .registerSubtype(Worm.class, "worm");

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

    public static void reforgeGameSave(@NonNull GameSave gameSave) {
        for (PhysicsBodySave saveBody : gameSave.entities) {
            for (TeamSave teamSave : gameSave.teams) {
                for (Worm worm : teamSave.team.getTeamWorms()) {
                    if (worm.uuid == saveBody.uuid) {
                        saveBody.physicsBody =  worm.getBody();
                        try {
                            Field teamField = Worm.class.getDeclaredField("team");
                            teamField.setAccessible(true);
                            Worm wormBody = (Worm) saveBody.entity;
                            teamField.set(wormBody, teamSave.team);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        for (PhysicsBodySave saveBody : gameSave.entities) {
            try {
            if (saveBody.entity instanceof Worm worm) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                    Field bodyField = Worm.class.getDeclaredField("body");
                    bodyField.setAccessible(true);
                    bodyField.set(worm, saveBody.physicsBody);
            } else if (saveBody.entity instanceof Projectile projectile) {
                // use reflexion to recreate worm instance with correct PhysicsBody
                Field bodyField = Projectile.class.getDeclaredField("body");
                bodyField.setAccessible(true);
                bodyField.set(projectile, saveBody.physicsBody);

            }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
