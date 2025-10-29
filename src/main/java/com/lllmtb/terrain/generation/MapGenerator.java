package com.lllmtb.terrain.generation;

import java.util.Random;

public class MapGenerator {
    public static int[] creationligne(int width, int height, int pentemax, int longeurmax) {
        int hauteur = height / 5;
        int x = 0;
        int pente = 0;
        int longeur = 0;
        int[] ligne = new int[width];
        Random random = new Random();
        while (x < width) {
            do {
                if (random.nextInt(101) < 40) { // 40% de chance d'avoir un plateau , parametre modifiable par la suite
                    pente = 0;
                } else {
                    pente = random.nextInt(pentemax - (-pentemax) + 1) + (-pentemax);
                }
                longeur = random.nextInt(longeurmax + 1);
            } while (hauteur + pente * longeur >= height || hauteur + pente * longeur < 0);
            for (int i = 0; i < longeur && x < width; i++) {
                ligne[x] = hauteur;
                hauteur += pente;
                x++;
            }
        }
        return ligne;
    }
}
