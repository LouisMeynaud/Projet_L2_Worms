package com.lllmtb.terrain;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;


public class SaveMap {
    public static void saveMap(Map m) {
        if (m.getClass().equals("undefined")) {     // ?? This should never be possible since m is a Map
            Scanner scanner = new Scanner(System.in);
            System.out.println("entré le nom du fichier d'enregistrement"); // ceci sera gerer par la classe event une fois crée

            boolean existe = false;
            String files;
            do {
                files = scanner.nextLine() + ".txt";

                String dossier = "src/main/java/com/lllmtb/terrain/MapFile/";

                File folder = new File(dossier);
                File[] fichiers = folder.listFiles();

                if (fichiers != null) {
                    for (File f : fichiers) {
                        if (f.getName().equals(files)) {
                            System.out.println(
                                    "Fichier déjà existant. Entrez O pour l’écraser, N pour en créer un nouveau.");
                            if (scanner.nextLine().equals("N")) {
                                existe = true;
                                System.out.println("merci d'entré un nouveaux nom de fichier");
                            }
                        }

                    }
                }
            } while (existe);
            writeText(m,files);
            scanner.close();
        } else {
            writeText(m,m.getFilesTxt());
        }
    }

    private static void writeText(Map m,String saveFile) {
        try (FileWriter writer = new FileWriter("src/main/java/com/lllmtb/terrain/MapFile/" + saveFile + ".txt")) {
            String texte = mapToString(m);
            writer.write(texte); 
            System.out.println("Map enregistrer");
        } catch (IOException e) {
            System.out.println(e);
        }

    }
    public static String mapToString(Map m){
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
