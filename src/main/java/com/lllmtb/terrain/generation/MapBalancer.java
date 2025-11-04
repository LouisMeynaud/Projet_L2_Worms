package com.lllmtb.terrain.generation;

public class MapBalancer {


    public static void leftRightBalancing(int[] lowLine, int[] hightLine, int height) {
        int width = hightLine.length;
        int mid = width / 2;
        int diffAccepted = 3;

        int leftAverage = averageThickness(lowLine, hightLine, 0, mid);
        int rightAverage = averageThickness(lowLine, hightLine, mid, width);
        int diff = leftAverage - rightAverage;

        if (diff >= diffAccepted) {//parametre a changer , plus diff est grand , moin le terrain risque d'etre equilibrer.
            // gauche plus épaisse , équilibrer en augmentant le côté droit
            for (int x = mid; x < width; x++) {
                int adjust = diff;
                System.out.println(diff);
                if (hightLine[x] - adjust > 0) {
                    hightLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) { 
                    lowLine[x] += adjust; // on agrandit vers le bas
                }
            }
        } else if (diff <= -diffAccepted){
            // droite plus épaisse , équilibrer en augmentant le côté gauche
            for (int x = 0; x < mid; x++) {
                int adjust = (-diff);
                System.out.println("adjust" + adjust);
                if (hightLine[x] - adjust > 0) {
                    hightLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) {
                    lowLine[x] += adjust;
                }
            }
        }
    }

    public static void thicknessBalencer(int thicknessMin,int epaisseur,int[] hightLine) {
        if(thicknessMin>epaisseur){
            for(int i =0;i<hightLine.length;i++){
                hightLine[i] += thicknessMin-epaisseur;
            }
        }
    }
    
    

    public static int averageThickness(int[] lowLine, int[] hightLine, int start, int end) { // epaisseur moyenne
        int somme = 0;
        for (int x = start; x < end; x++) {
            somme += (lowLine[x] - hightLine[x]);
        }
        return somme / (end - start);
    }
}
