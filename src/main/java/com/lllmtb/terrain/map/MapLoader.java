package com.lllmtb.terrain.map;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.save.SaveConfig;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapLoader {

    /**
     * Load a map from a given map name.
     * @param directoryName the name of the directory where the map is stored.
     * @return the loaded Map object.
     */
    public static Map loadMapLoad(String directoryName) {
        try {
            String stringPath = SaveConfig.SAVE_DIRECTORY + directoryName;
            if (directoryName.contains("Test")) {
                stringPath = "src/test/resources/savesTest/" + directoryName;
            }
            Path path = Paths.get(stringPath + "/map.txt");

            if (!Files.exists(path)) {
                System.err.println("Aucune sauvegarde trouvée à : " + path.toAbsolutePath());
                return null;
            }

            // Lecture du fichier externe
            String contentMap = Files.readString(path, StandardCharsets.UTF_8);
            return loadMatrix(contentMap, directoryName);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la sauvegarde : " + e.getMessage());
            return null;
        }
    }

    /**
     * Load a map from a given map name.
     * @param mapName the name of the map to load.
     * @return the loaded Map object.
     */
    public static Map loadMap(String mapName) {
        try (InputStream is = MapLoader.class.getResourceAsStream("/map/" + mapName + "/map.txt")) {
            if (is == null) {
                throw new RuntimeException("Resource not found: /map/" + mapName + "/map.txt");
            }
            String contentMap = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return loadMatrix(contentMap, mapName);
        } catch (Exception e) {
            Logger.logger.error("Error while loading map {} : ", mapName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * transform a string representation of map in a instance of map.
     * @param contentMap the String representation of map
     * @param name the name of the
     * @return the instance of the map
     */
    public static Map loadMatrix(String contentMap, String name) {
            String[] row = contentMap.split("\n");
            Map tileMatrix = new Map(row.length, row[0].length(), name + ".txt");

            for (int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[0].length(); j++) {
                        try {
                            tileMatrix.getMapMatrix()[i][j] = Tile.getByFileSymbol(row[i].charAt(j));

                        } catch (IllegalArgumentException e) {
                            Logger.logger.error("Invalid tile symbol at the position x" + j + " y" + i + " : " + row[i].charAt(j));
                            throw new IllegalArgumentException("Invalid tile symbol at position (" + j + ", " + i + "): " + row[i].charAt(j), e);
                        }
                }
            }
            tileMatrix.initializeHeightWater();
            return tileMatrix;
    }
    /**
     * Get a list of available map names.
     * @return Array of map names.
     */
    public static String[] getListeNomsMaps() {
        return new String[] {"Chaussée aux moines", "Pandora", "Camembert", "Emmental", "Gorgonzola", "Roquefort", "Feta", "Cendré de Lune" };
    }
}