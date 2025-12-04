package com.lllmtb.engine;

import com.lllmtb.terrain.map.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lllmtb.terrain.map.SaveMap;

public class Save {
    protected static GameWorld gameWorld;

    protected static void setGameWorld(GameWorld gWorld) {
        gameWorld = gWorld;
    }

    public static void SaveGameWorld(GameWorld gameWorld, String stringPath) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        GameSave gameSave = new GameSave(gameWorld);
        String json =  gson.toJson(gameSave);
        Path path = Paths.get(stringPath);
        Files.createDirectories(path);
        Path folderPath = path.resolve("test.json");
        try {
            System.out.println("Game World sauvegardé dans: " + stringPath);
            Files.write(folderPath, json.getBytes());

        }
       catch (IOException e) {e.printStackTrace();} catch (Exception e) {e.printStackTrace();}

    }

    private static  boolean SaveHightline(Map map, String stringPath) throws IOException {
        try {
            Path path = Paths.get(stringPath);//concataine
            Files.createDirectories(path);
            Path filePath = path.resolve("hightline.txt");
            Files.writeString(filePath, map.HightLineToString());

            System.out.println("Succès : Hightline sauvegardée dans " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static  boolean SaveMap(Map map, String stringPath) throws IOException {
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

    public static void SaveAll() throws IOException {
        String stringPath = "src/test/resources/SaveTest/" + gameWorld.getGameName();
        SaveHightline(gameWorld.getMap(),stringPath );
        SaveMap(gameWorld.getMap(), stringPath);
        SaveGameWorld(gameWorld, stringPath);

    }
}
