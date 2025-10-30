package com.lllmtb.terrain.generation;

public class MapBalancer {
    public static void leftRightBalancing(int[] lowLine, int[] TallLine, int width, int height) {
        int mid = width / 2;

        int leftAverage = averageThickness(lowLine, TallLine, 0, mid);
        int rightAverage = averageThickness(lowLine, TallLine, mid, width);
        System.out.println("moyenne gauche" + leftAverage);
        System.out.println("moyenne droite" + rightAverage);
        int diff = leftAverage - rightAverage;
        System.out.println("diff" + diff);

        if (diff >= 3) {
            // gauche plus épaisse → équilibrer en augmentant le côté droit
            for (int x = mid; x < width; x++) {
                int adjust = diff;
                System.out.println(diff);
                if (TallLine[x] - adjust > 0) {
                    TallLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) { 
                    lowLine[x] += adjust; // on agrandit vers le bas
                }
            }
        } else if (diff <= -3){
            // droite plus épaisse → équilibrer en augmentant le côté gauche
            for (int x = 0; x < mid; x++) {
                int adjust = (-diff);
                System.out.println("adjust" + adjust);
                if (TallLine[x] - adjust > 0) {
                    TallLine[x] -= adjust;
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
