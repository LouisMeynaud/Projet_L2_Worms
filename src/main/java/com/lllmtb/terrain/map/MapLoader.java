package com.lllmtb.terrain.map;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapLoader {

    /**
     * Load a map from a given map name.
     * @param mapName
     * @return the loaded Map object.
     */
    public static Map loadMatrix(String mapName) {
        try {
            URL resource = MapLoader.class.getResource("/map/" + mapName + "/map.txt");
            if (resource == null) {
                return null;
            }
            Path path = Paths.get(resource.toURI());
            String contentMap = Files.readString(path);
            String[] row = contentMap.split("\n");
            Map tileMatrix = new Map(row.length, row[0].length(), mapName + ".txt");

            for (int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[0].length(); j++) {
                        tileMatrix.getMapMatrix()[i][j] = Tile.getByFileSymbol(row[i].charAt(j));
                    }
                }
            tileMatrix.initializeHeightWater();
            return tileMatrix;

        } catch (Exception e) {
            System.out.println(e); // NB. Il faudra mieux gérer cette exception
            return null;
        }
    }

    public static String[] getListeNomsMaps() {
        return new String[] {"Camembert", "Emmental", "Gorgonzola", "Roquefort" };
    }
}