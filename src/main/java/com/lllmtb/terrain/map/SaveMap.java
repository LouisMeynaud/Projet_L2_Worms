package com.lllmtb.terrain.map;

//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

public class SaveMap {
//  public static boolean saveMap(Map map) {
//     try {

//         Path path = Paths.get("src/main/resources/map");
//         Path folderPath = path.resolve(map.getFilesTxt());//concataine
//         Files.createDirectories(folderPath);
//         Path filePath = folderPath.resolve("map.txt");
//         Files.writeString(filePath, mapToString(map));

//         System.out.println("Succès : Map sauvegardée dans " + filePath);
//         return true;
//     } catch (IOException e) {
//         System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
//         e.printStackTrace();
//         return false;
//     }
// }

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
        // boolean existe = false;
        // String files;
        // do {
        //     existe = false;
        //     files = "testMap.txt";;

        //     String dossier = "src/main/java/com/lllmtb/terrain/MapFile/";

        //     File folder = new File(dossier);
        //     File[] fichiers = folder.listFiles();

        //     if (fichiers != null) {
        //         for (File f : fichiers) {
        //             if (f.getName().equals(files)) {
        //                 System.out.println("Fichier déjà existant. Entrez O pour l’écraser, N pour en créer un nouveau.");
        //             }
        //         }
        //     }
        // } while (existe);
        // return files;
         return "testMap.txt";

     }
}
