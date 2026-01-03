package com.lllmtb.engine.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lllmtb.terrain.map.SaveMap;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.ui.graphical.TextureManager;

import javax.imageio.ImageIO;

/**
 * Utility class responsible for game data persistence.
 * <p>
 * This class handles saving the state of the {@link GameWorld}, the terrain configuration ({@link Map}),
 * the heightline, and game meta-information. Data is exported in both JSON and text formats.
 * </p>
 */
public class Save {

    /** Static instance of the game world used for global save operations. */
    protected static GameWorld gameWorld;

    /**
     * Sets the static GameWorld instance to be used by the save methods.
     *
     * @param gWorld The current {@link GameWorld} instance to save.
     */
    public static void setGameWorld(GameWorld gWorld) {
        gameWorld = gWorld;
    }

    /**
     * Saves the complete state of the GameWorld to a JSON file.
     * <p>
     * This method uses GSON with a {@link RuntimeTypeAdapterFactory} to handle entity polymorphism
     * (e.g., differentiating between {@link Projectile} and {@link Worm}).
     * The output file is named {@code test.json}.
     * </p>
     *
     * @param gameWorld  The {@link GameWorld} object containing the current game state.
     * @param stringPath The directory path where the JSON file will be created.
     * @throws IOException If an error occurs during directory creation or file writing.
     */
    public static void saveGameWorld(GameWorld gameWorld, String stringPath) throws IOException {
        RuntimeTypeAdapterFactory<Entity> entityAdapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Projectile.class, "projectile")
                .registerSubtype(Worm.class, "worm")
                .registerSubtype(Coin.class, "coin")
                .registerSubtype(Drop.class, "drop");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(entityAdapter)
                .serializeSpecialFloatingPointValues()
                .create();
        GameSave gameSave = new GameSave(gameWorld);
        String json =  gson.toJson(gameSave);
        Path path = Paths.get(stringPath);
        try {
            Files.createDirectories(path);
            Path folderPath = path.resolve("save.json");
            System.out.println("Game World sauvegardé dans: " + stringPath);
            Files.write(folderPath, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the map structure to a text file.
     * The output file is named {@code map.txt}.
     *
     * @param map        The {@link Map} object to save.
     * @param stringPath The destination directory path.
     * @return {@code true} if the save was successful, {@code false} otherwise.
     * @throws IOException If a critical I/O error occurs.
     */
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

    /**
     * Saves general game information (Name, Teams, Current Turn) to a text file.
     * The output file is named {@code info.txt}.
     *
     * @param gameWorld  The GameWorld source for the information.
     * @param stringPath The destination directory path.
     */
    public static  void saveInfo(GameWorld gameWorld, String stringPath) {
        try {
            Path path = Paths.get(stringPath);
            Files.createDirectories(path);
            Path filePath = path.resolve("info.txt");
            Files.writeString(filePath, info(gameWorld));

            System.out.println("Succès : Info sauvegardée dans " + filePath);

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * Generates a string representation of the game's meta-information.
     *
     * @param gameWorld The source GameWorld.
     * @return A formatted string containing the game name, team list, and the team currently playing.
     */
    private static String  info(GameWorld gameWorld)  {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateFormatee = time.format(formatter);
        String info = "";
        info +=  "Partie Sauvegardé le: " + dateFormatee + "; ";
        info += "Tour actuel: "  + gameWorld.getTurnManager().getNbTurn() + "; ";

        info +=  "L'équipe " + gameWorld.getPlayingTeam().name + " joue.";
        return info;
    }

    public static BufferedImage maptoBuffered(Map map) throws IOException {
        int tileSize = 12;
        int width = map.getWidth() * tileSize;
        int height = map.getHeight() * tileSize;

        BufferedImage mapCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mapCache.createGraphics(); //cree un crayon dans mapCache

        g2d.setColor(new Color(135, 206, 235));
        g2d.fillRect(0, 0, width, height);
        Tile[][] mapMatrix = map.getMapMatrix();

        for (int y = 0; y < mapMatrix.length; y++) {
            for (int x = 0; x < mapMatrix[y].length; x++) {
                Tile tileType = mapMatrix[y][x];
                String type = tileType.toString() + "0";


                BufferedImage texture = TextureManager.TILES.get(type);

                if (texture != null) {
                    int screenX = x * tileSize;
                    int screenY = y * tileSize;
                    g2d.drawImage(texture, screenX, screenY, tileSize, tileSize, null);
                }
            }
        }
        g2d.dispose();

        return mapCache;
    }

    public static void saveImage(BufferedImage image, String stringPath) throws IOException {
        if (image == null) {
            System.err.println("Erreur : L'image à sauvegarder est nulle !");
            return;
        }

        try {
            File outputFile = new File(stringPath + "/image.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Succès : Image sauvegardée dans" + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur d'écriture : " + e.getMessage());
        }
    }

    /**
     * Orchestrates the complete save process for all game elements.
     * <p>
     * This method creates a directory based on the game name inside {@code src/test/resources/SaveTest/}
     * and sequentially calls save methods for the Hightline, Map, Info, and GameWorld (JSON).
     * </p>
     *
     * @throws IOException If an error occurs during any of the save processes.
     */
    public static void saveAll() throws IOException {
        String stringPath = SaveConfig.SAVE_DIRECTORY + gameWorld.getGameName();
        saveMap(gameWorld.getMap(), stringPath);
        saveGameWorld(gameWorld, stringPath);
        saveInfo(gameWorld, stringPath);
        BufferedImage image = maptoBuffered(gameWorld.getMap());
        saveImage(image, stringPath);
        gameWorld.pause(false);
    }

    public static void eraseSave(String name) {
        Path pathToBeDeleted = Paths.get(SaveConfig.SAVE_DIRECTORY, name);

        try {
            if (Files.exists(pathToBeDeleted)) {
                Files.walk(pathToBeDeleted)
                        .sorted(java.util.Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Impossible de supprimer : " + path + " - " + e.getMessage());
                            }
                        });
                System.out.println("Sauvegarde '" + name + "' supprimée avec succès.");
            } else {
                System.out.println("La sauvegarde '" + name + "' n'existe pas.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
