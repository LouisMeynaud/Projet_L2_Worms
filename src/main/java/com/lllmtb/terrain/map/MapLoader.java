package com.lllmtb.terrain.map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {
    public static Map loadMatrix(String mapName) {
        try {
            String path = "src/main/java/com/lllmtb/terrain/MapFile/" + mapName + ".txt";
            String content = Files.readString(Path.of(path));
            String[] row = content.split("\n");

            Map tileMatrix = new Map(row.length, row[0].length(), mapName);
            for (int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[0].length(); j++) {
                    switch (row[i].charAt(j)) { // perhaps create another `Map.setTile(x, y)` that could be used
                                                // here
                        case 'D':
                            tileMatrix.getMapMatrix()[i][j] = Tile.DIRT;
                            break;
                        case 'W':
                            tileMatrix.getMapMatrix()[i][j] = Tile.WATER;
                            break;
                        case 'A':
                            tileMatrix.getMapMatrix()[i][j] = Tile.AIR;
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "attention,la lettre" + content.charAt(i) + "ne correspond a aucun Tile");
                    }

                }
            }
            return tileMatrix;

        } catch (IOException e) {
            System.out.println(e); // NB. Il faudra mieux gérer cette exception
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException : " + e.getMessage());
            return null; // il faudra gerer cette erreur par la suite;
        }
    }
}
