package com.lllmtb.terrain.map;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class SaveMap {
   public static boolean saveMap(Map m) {
    try {
        File file = new File("src/main/java/com/lllmtb/terrain/MapFile/" + m.getFilesTxt());
        file.createNewFile();    //Creates the file if it doesn't exist, overwrites it otherwise
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

    protected static String mapToString(Map m) {
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

    //methode demandant le fichie d'enregistrement de save quand une map est crée
    public static String newMapSaveFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("entré le nom du fichier d'enregistrement");

        boolean existe = false;
        String files;
        do {
            existe = false;
            files = scanner.nextLine()+".txt";;

            String dossier = "src/main/java/com/lllmtb/terrain/MapFile/";

            File folder = new File(dossier);
            File[] fichiers = folder.listFiles();

            if (fichiers != null) {
                for (File f : fichiers) {
                    if (f.getName().equals(files)) {
                        System.out.println("Fichier déjà existant. Entrez O pour l’écraser, N pour en créer un nouveau.");
                                if (scanner.nextLine().equals("N")) {
                            existe = true;
                            System.out.println("merci d'entré un nouveaux nom de fichier");
                        }
                    }

                }
            }
        } while (existe);
        scanner.close();
        return files;

    }
}
