package com.lllmtb.terrain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {
    public static Map createMatrix(String maptext) {
        try {
            String path = "src/main/java/com/lllmtb/terrain/MapFile/" + maptext + ".txt";
            String mapText = Files.readString(Path.of(path));
            System.out.println(mapText);
            String[] ligne = mapText.split("\n");

            Map TILESMatrix = new Map(ligne.length, ligne[0].length());
            for (int i = 0; i < ligne.length; i++) {
                for (int j = 0; j < ligne[0].length(); j++) {
                    if (ligne[i].charAt(j) != '\n') {
                        System.out.println(ligne[i].charAt(j));
                        switch (ligne[i].charAt(j)) {
                            case 'D':
                                TILESMatrix.getMapMatrix()[i][j] = Tile.TILES.DIRT;
                                break;
                            case 'W':
                                TILESMatrix.getMapMatrix()[i][j] = Tile.TILES.valueOf("WATER");
                                break;
                            case 'A':
                                TILESMatrix.getMapMatrix()[i][j] = Tile.TILES.valueOf("AIR");
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "attention,la lettre" + mapText.charAt(i) + "ne correspond a aucun TILES");
                        }
                    }

                }
            }
            return TILESMatrix;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
