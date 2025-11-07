package com.lllmtb.terrain.generation;

/**
 * classe permetant un equilibrage de la carte apres generation aleratoire afin
 * de garder une coherence et equiter entre les deux coter et donc les deux
 * equipe
 */
public class MapBalancer {

    /**
     * augmente/diminue l'epaisseur de la partit gauche/droite de lowLine/heightLine afin de garder une equiter entre l'equipe gauche te la droite. 
     * @param lowLine la ligne basse de la map
     * @param hightLine la ligne haute de la map
     * @param height la hauteur de la map (permet a hightLine de ne pas la depasser);
     * 
     */
    public static void leftRightBalancing(final int[] lowLine, final int[] hightLine, final int height) {
        final int width = hightLine.length;
        final int mid = width / 2;
        final int diffAccepted = 3;

        final int leftAverage = averageThickness(lowLine, hightLine, 0, mid);
        final int rightAverage = averageThickness(lowLine, hightLine, mid, width);
        final int diff = leftAverage - rightAverage;

        if (diff >= diffAccepted) {// parametre a changer , plus diff est grand , moin le terrain risque d'etre
                                   // equilibrer.
            // gauche plus épaisse , équilibrer en augmentant le côté droit
            for (int x = mid; x < width; x++) {
                final int adjust = diff;
                System.out.println(diff);
                if (hightLine[x] - adjust > 0) {
                    hightLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) {
                    lowLine[x] += adjust; // on agrandit vers le bas
                }
            }
        } else if (diff <= -diffAccepted) {
            // droite plus épaisse , équilibrer en augmentant le côté gauche
            for (int x = 0; x < mid; x++) {
                final int adjust = (-diff);
                System.out.println("adjust" + adjust);
                if (hightLine[x] - adjust > 0) {
                    hightLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) {
                    lowLine[x] += adjust;
                }
            }
        }
    }

    /**
     * methode permettant d'eviter les map trop fine en introduisant une epaisseur minimum.
     * @param thicknessMin l'epaisseur minimum souhaiter
     * @param epaisseur l'epaisseur actuelle de la carte
     * @param hightLine la ligne huate que l'on augmenteras afin d'avoir l'epaisseur minimun voulu
     */
    public static void thicknessBalencer(final int thicknessMin, final int epaisseur, final int[] hightLine) {
        if (thicknessMin > epaisseur) {
            for (int i = 0; i < hightLine.length; i++) {
                if (hightLine[i] - (thicknessMin - epaisseur) > 0) {
                    hightLine[i] -= thicknessMin - epaisseur;
                }
            }
        }
    }
    /**
     * methode permettant de calculer l'epaisser d'un terrain d'une position start a end
     * @param lowLine la ligne basse
     * @param hightLine la ligne haute
     * @param start le debut de la zonne ou le calcule est souhaiter
     * @param end la fin de la zone ou le calcule est souhaiter
     * @return l'epaisseur de la carte sous forme de int
     */
    public static int averageThickness(final int[] lowLine, final int[] hightLine, final int start, final int end) { // epaisseur moyenne
        int somme = 0;
        for (int x = start; x < end; x++) {
            somme += (lowLine[x] - hightLine[x]);
        }
        return (int) somme / (end - start);
    }
}
