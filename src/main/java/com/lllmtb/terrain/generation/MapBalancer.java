package com.lllmtb.terrain.generation;

import java.util.Random;

public class MapBalancer {
    public static void equilibrageGaucheDroite(int[] ligneBasse, int[] ligneHaute, int width, int height) {
        int mid = width / 2;

        // calcul des moyennes
        int moyenneGauche = moyenneEpaisseur(ligneBasse, ligneHaute, 0, mid);
        int moyenneDroite = moyenneEpaisseur(ligneBasse, ligneHaute, mid, width);

        int diff = moyenneGauche - moyenneDroite;

        if (Math.abs(diff) > 3) { // si diff epaisseur trop grande.
            if (diff > 0) {
                // gauche plus épaisse
                for (int x = 0; x < mid; x++)
                    if (ligneHaute[x] + diff / 2 < height) {
                        ligneHaute[x] += diff / 2;
                    } else if (ligneBasse[x] - diff / 2 > 0) {
                        ligneBasse[x] -= diff / 2;
                    }
            } else {
                // droite plus épaisse
                for (int x = mid; x < width; x++)
                    if (ligneHaute[x] + diff / 2 < height) {
                        ligneHaute[x] += diff / 2;
                    } else if (ligneBasse[x] - diff / 2 > 0) {
                        ligneBasse[x] -= diff / 2;
                    }
            }
        }
    }

    private static int moyenneEpaisseur(int[] ligneBasse, int[] ligneHaute, int debut, int fin) {
        int somme = 0;
        for (int x = debut; x < fin; x++) {
            somme += (ligneHaute[x] - ligneBasse[x]);
        }
        return somme / (fin - debut);
    }
}
