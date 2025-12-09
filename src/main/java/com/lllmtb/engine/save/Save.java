package com.lllmtb.engine.save;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.BazookaWeapon;
import com.lllmtb.model.item.weapon.GunWeapon;
import com.lllmtb.model.item.weapon.Weapon;
import com.lllmtb.terrain.map.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllmtb.terrain.map.SaveMap;

public class Save {
    protected static GameWorld gameWorld;

    public static void setGameWorld(GameWorld gWorld) {
        gameWorld = gWorld;
    }

    public static void saveGameWorld(GameWorld gameWorld, String stringPath) throws IOException {
        RuntimeTypeAdapterFactory<Entity> entityAdapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Projectile.class, "projectile") // Si le JSON contient "type": "bazooka", on crée un Bazooka
                .registerSubtype(Worm.class, "worm");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(entityAdapter)
                .create();
        GameSave gameSave = new GameSave(gameWorld);
        String json =  gson.toJson(gameSave);
        Path path = Paths.get(stringPath);
        try {
            Files.createDirectories(path);
            Path folderPath = path.resolve("test.json");
            System.out.println("Game World sauvegardé dans: " + stringPath);
            Files.write(folderPath, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean saveHightline(Map map, String stringPath) throws IOException {
        try {
            Path path = Paths.get(stringPath); //concataine
            Files.createDirectories(path);
            Path filePath = path.resolve("hightline.txt");
            Files.writeString(filePath, map.hightLineToString());

            System.out.println("Succès : Hightline sauvegardée dans " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static  boolean saveMap(Map map, String stringPath) throws IOException {
        try {
            Path path = Paths.get(stringPath);
            Files.createDirectories(path);
            Path filePath = path.resolve("map.txt");
            Files.writeString(filePath, SaveMap.mapToString(map));

            System.out.println("Succès : Map sauvegardée dans " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unused")
    public static void saveAll() throws IOException {
        String stringPath = "src/test/resources/SaveTest/" + gameWorld.getGameName();
        saveHightline(gameWorld.getMap(), stringPath);
        saveMap(gameWorld.getMap(), stringPath);
        saveGameWorld(gameWorld, stringPath);
    }
}
