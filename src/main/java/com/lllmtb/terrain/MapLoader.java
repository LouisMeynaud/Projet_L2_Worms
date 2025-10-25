package com.lllmtb.terrain;


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
}
