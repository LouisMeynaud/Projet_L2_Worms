package com.lllmtb.terrain;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class SaveMap {
   public static boolean saveMap(Map m) {
    try {
        File file = new File("src/main/java/com/lllmtb/terrain/MapFile/" + m.getFilesTxt() + ".txt");
        file.createNewFile();    //crée le fichier quand il existe pas      
        FileWriter writer = new FileWriter(file);
        writer.write(mapToString(m));
        writer.close();

        System.out.println("Map enregistrée");
        return true;
    } catch (IOException e) {
        System.out.println("Erreur : " + e.getMessage());
        return false;
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
                        mapString.append("D");break;
                    default : throw new IllegalStateException("Tile inconnue : " + t);
                }
            }
            mapString.append("\n");
        }
        return mapString.toString();
    }
}
