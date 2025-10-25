package com.lllmtb.terrain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MapLoader {
    private static int heightCalcul(String matrice) {
        int compteurHauteur = -1;
        for (int i = 0; i < matrice.length(); i++) {
            if (matrice.charAt(i) == '[') {
                compteurHauteur += 1;
            }
        }
        return compteurHauteur;
    }

    private static int widthCalcul(String matrice) {
        int compteurLargeur = 1;
        int j = 0;
        while (matrice.charAt(j) != ']') {
            if (matrice.charAt(j) == ',') {
                compteurLargeur += 1;
            }
            j++;
        }
        return compteurLargeur;
    }


    public static Map createMatrix(String nameMap) {
        try {
        String path = "src/main/java/com/lllmtb/terrain/FichierCarte/" + nameMap;
        String mapText = Files.readString(Path.of(path));

        Map blocMatrix = new Map(heightCalcul(mapText),widthCalcul(mapText));
        int z = 0;
        int a = 0;

        for (int i = 0; i < mapText.length(); i++) {
            if ((mapText.charAt(i) != '[' && mapText.charAt(i) != ']') && mapText.charAt(i) != ','
                    && mapText.charAt(i) != '\n') {
                switch (mapText.charAt(i)) {
                    case 'T':
                        blocMatrix.getMapMatrix()[z][a] = Map.Bloc.valueOf("DIRT");break;
                    case 'E':
                        blocMatrix.getMapMatrix()[z][a] = Map.Bloc.valueOf("WATER");break;
                    case 'A':
                        blocMatrix.getMapMatrix()[z][a] = Map.Bloc.valueOf("AIR");break;
                default: throw new IllegalArgumentException("attention,la lettre"+mapText.charAt(i)+"ne correspond a aucun bloc");
                }

                a++;
                if (a % blocMatrix.getMapMatrix()[0].length == 0) {
                    z++;
                    a = 0;
                }
            }
        }
        return blocMatrix;
    } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
