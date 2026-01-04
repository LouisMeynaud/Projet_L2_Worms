package com.lllmtb.terrain.map;

import com.lllmtb.engine.save.SaveConfig;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapLoader {

    /**
     * Load a map from a given map name.
     * @param directoryName
     * @return the loaded Map object.
     */

    public static Map loadMapLoad(String directoryName) {
        try {
            Path path = Paths.get(SaveConfig.SAVE_DIRECTORY + directoryName + "/map.txt");

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
     * @param mapName
     * @return the loaded Map object.
     */
    public static Map loadMap(String mapName) {
        try {
            URL resource = MapLoader.class.getResource("/map/" + mapName + "/map.txt");
            if (resource == null) {
                return null;
            }
            Path path = Paths.get(resource.toURI());
            String contentMap = Files.readString(path);
            return  loadMatrix(contentMap, mapName);
        } catch (Exception e) {
            System.out.println(e); // NB. Il faudra mieux gérer cette exception
            return null;
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
                        tileMatrix.getMapMatrix()[i][j] = Tile.getByFileSymbol(row[i].charAt(j));
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
        return new String[] {"Camembert", "Emmental", "Gorgonzola", "Roquefort", "Feta", "Cendré de Lune" };
    }
}