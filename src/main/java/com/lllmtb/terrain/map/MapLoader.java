package com.lllmtb.terrain.map;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {

    /**
     * Load a map from a given map name.
     * @param mapName
     * @return the loaded Map object.
     */
    public static Map loadMatrix(String mapName) {
        String path;
        if (mapName.contains("Test")) {
            path = "src/test/resources/map/";
        } else if (mapName.contains("Save")) {
            path = "src/test/resources/SaveTest/";
    } else {
            path = "src/main/resources/map/";
        }

        try {
            String mapPath = path + mapName + "/map.txt";
            System.out.println("TEST " + mapPath);
            String contentMap = Files.readString(Path.of(mapPath));
            String[] row = contentMap.split("\n");
            Map tileMatrix = new Map(row.length, row[0].length(), mapName + ".txt");

            for (int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[0].length(); j++) {
                        tileMatrix.getMapMatrix()[i][j] = Tile.getByFileSymbol(row[i].charAt(j));
                    }
                }
            return tileMatrix;

        } catch (IOException e) { // we should propagate the exception instead so GameWorld can catch and respond accordingly
            return null;
        } catch (Exception e) {
            System.out.println(e); // NB. Il faudra mieux gérer cette exception
            return null;
        }
    }

    /**
     * Gets the list of map directories.
     * @return an array of map directories.
     */
    public static File[] getListeDossiersMaps() {
        String cheminDossierMaps = "src/main/resources/map";
        return getListeDossiers(new File(cheminDossierMaps));
    }

    /**
     * Gets the list of subdirectories in a given root directory.
     * @param dossierRacine the root directory.
     * @return an array of subdirectories.
     */
    public static File[] getListeDossiers(File dossierRacine) {
        if (!dossierRacine.exists() || !dossierRacine.isDirectory()) {
            System.err.println("Erreur le chemin n'est pas bon : " + dossierRacine.getAbsolutePath());
            return new File[0];
        }
        File[] sousDossiers = dossierRacine.listFiles(f -> MapLoader.predicatEstUnDossier(f));

        if (sousDossiers == null) {
            return new File[0];
        }
        return sousDossiers;
    }

    private static boolean predicatEstUnDossier(File fichier) {
        return fichier.isDirectory();
    }


}
