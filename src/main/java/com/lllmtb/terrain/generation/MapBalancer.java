package com.lllmtb.terrain.generation;


public class MapBalancer {
    public static void leftRightBalancing(int[] lowLine, int[] TallLine, int width, int height) {
        int mid = width / 2;

        // calcul des moyennes
        int leftAverage = averageThickness(lowLine, TallLine, 0, mid);
        int rightAverage = averageThickness(lowLine, TallLine, mid, width);

        int diff = leftAverage - rightAverage;

        if (Math.abs(diff) > 3) { // si diff epaisseur trop grande.
            if (diff > 0) {
                // gauche plus épaisse
                for (int x = 0; x < mid; x++)
                    if (TallLine[x] + diff / 2 < height) {
                        TallLine[x] += diff / 2;
                    } else if (lowLine[x] - diff / 2 > 0) {
                        lowLine[x] -= diff / 2;
                    }
            } else {
                // droite plus épaisse
                for (int x = mid; x < width; x++)
                    if (TallLine[x] + diff / 2 < height) {
                        TallLine[x] += diff / 2;
                    } else if (lowLine[x] - diff / 2 > 0) {
                        lowLine[x] -= diff / 2;
                    }
            }
        }
    }

    private static int averageThickness(int[] lowLine, int[] TallLine, int start, int end) { //epaisseur moyenne
        int somme = 0;
        for (int x = start; x < end; x++) {
            somme += (TallLine[x] - lowLine[x]);
        }
        return somme / (end - start);
    }
}
