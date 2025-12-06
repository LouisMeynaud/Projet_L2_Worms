package com.lllmtb.terrain.map;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {

    public static Map loadMatrix(String mapName) {
        String path;
        if (mapName.contains("Test")) {
            path = "src/test/resources/map/";
        } else {
            path = "src/main/resources/map/";
        }

        try {
            String mapPath = path + mapName + "/map.txt";
            System.out.println("TEST " + mapPath);
            String contentMap = Files.readString(Path.of(mapPath));
            String[] row = contentMap.split("\n");

            String hightLinePath = path + mapName + "/hightLine.txt";
            String contentHightLine = Files.readString(Path.of(hightLinePath));
            String[] stringtabHightLine = contentHightLine.split(",");
            int[] intTabHightLine = new int[stringtabHightLine.length];
            for (int i = 0; i < stringtabHightLine.length; i++) {
                intTabHightLine[i] = Integer.valueOf(stringtabHightLine[i]);
            }
            Map tileMatrix = new Map(row.length, row[0].length(), mapName + ".txt");
            tileMatrix.setHightLine(intTabHightLine);

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
                        case 'C':
                            tileMatrix.getMapMatrix()[i][j] = Tile.CAVERN;
                            break;
                        case 'G':
                            tileMatrix.getMapMatrix()[i][j] = Tile.GRASS;
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "attention,la lettre" + contentHightLine.charAt(i) + "ne correspond a aucun Tile");
                    }

                }
                //tileMatrix.setHightLine(hightLine);
            }
            return tileMatrix;

        } catch (IOException e) { // we should propagate the exception instead so GameWorld can catch and respond accordingly
            return null;
        } catch (Exception e) {
            System.out.println(e); // NB. Il faudra mieux gérer cette exception
            return null;
        }
    }


    public static File[] getListeDossiersMaps() {
        String cheminDossierMaps = "src/main/resources/map";
        return getListeDossiers(new File(cheminDossierMaps));
    }

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
