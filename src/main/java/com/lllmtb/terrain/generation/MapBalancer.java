package com.lllmtb.terrain.generation;

public class MapBalancer {


    public static void leftRightBalancing(int[] lowLine, int[] tallLine, int width, int height) {
        int mid = width / 2;
        int diffAccepted = 3;

        int leftAverage = averageThickness(lowLine, tallLine, 0, mid);
        int rightAverage = averageThickness(lowLine, tallLine, mid, width);
        int diff = leftAverage - rightAverage;

        if (diff >= diffAccepted) {//parametre a changer , plus diff est grand , moin le terrain risque d'etre equilibrer.
            // gauche plus épaisse , équilibrer en augmentant le côté droit
            for (int x = mid; x < width; x++) {
                int adjust = diff;
                System.out.println(diff);
                if (tallLine[x] - adjust > 0) {
                    tallLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) { 
                    lowLine[x] += adjust; // on agrandit vers le bas
                }
            }
        } else if (diff <= -diffAccepted){
            // droite plus épaisse , équilibrer en augmentant le côté gauche
            for (int x = 0; x < mid; x++) {
                int adjust = (-diff);
                System.out.println("adjust" + adjust);
                if (tallLine[x] - adjust > 0) {
                    tallLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) {
                    lowLine[x] += adjust;
                }
            }
        }
    }
    
    

    public static int averageThickness(int[] lowLine, int[] TallLine, int start, int end) { // epaisseur moyenne
        int somme = 0;
        for (int x = start; x < end; x++) {
            somme += (lowLine[x] - TallLine[x]);
        }
        return somme / (end - start);
    }
}
