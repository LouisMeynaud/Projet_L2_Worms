package com.lllmtb.terrain;

import java.io.IOException;
import java.io.FileWriter;

public class SaveMap {
    public static void saveMap(Map m) {
        try (FileWriter writer = new FileWriter(
                "src/main/java/com/lllmtb/terrain/MapFile/" + m.getFilesTxt() + ".txt")) {
            String texte = mapToString(m);
            writer.write(texte);
            System.out.println("Map enregistrer");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String mapToString(Map m) {
        Tile[][] mMapMatrix = m.getMapMatrix();
        StringBuilder mapString = new StringBuilder();
        for (Tile[] mapRow : mMapMatrix) {
            for (Tile t : mapRow) {
                switch (t) {
                    case AIR:
                        mapString.append("A");
                        break;
                    case WATER:
                        mapString.append("W");
                        break;
                    case DIRT:
                        mapString.append("D");
                        break;
                }
            }
            mapString.append("\n");
        }
        return mapString.toString();
    }
}
