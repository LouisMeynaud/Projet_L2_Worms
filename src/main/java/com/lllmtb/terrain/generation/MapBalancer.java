package com.lllmtb.terrain.generation;

/**
* A class that allows for map balancing after random generation in order to
* maintain consistency and fairness between the two sides and therefore the two
* teams
 */
public class MapBalancer {

    /**
     * 
    *  increases/decreases the thickness of the left/right part of lowLine/heightLine in order to maintain fairness between the left and right team.
     * @param lowLine 
     * @param hightLine 
     * @param height the height of the map (allows highLine not to exceed it);
     * 
     */
    public static void leftRightBalancing(final int[] lowLine, final int[] hightLine, final int height) {
        final int width = hightLine.length;
        final int mid = width / 2;
        final int diffAccepted = 3;

        final int leftAverage = averageThickness(lowLine, hightLine, 0, mid);
        final int rightAverage = averageThickness(lowLine, hightLine, mid, width);
        final int diff = leftAverage - rightAverage;

        if (diff >= diffAccepted) {// Parameter to change: the larger the difference, the less likely the terrain is to be balanced. Left side thicker, balance by increasing the right side.
            for (int x = mid; x < width; x++) {
                final int adjust = diff;
                System.out.println(diff);
                if (hightLine[x] - adjust > 0) {
                    hightLine[x] -= adjust;
                } else if (lowLine[x] + adjust < height) {
                    lowLine[x] += adjust; // we enlarge downwards
                }
            }
        } else if (diff <= -diffAccepted) {
            //Thicker right side, balance by increasing the left side
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
     * method to avoid excessively thin maps by introducing a minimum thickness.
     * @param thicknessMin the minimum desired thickness
     * @param epaisseur the current thickness of the card
     * @param hightLine the upper line which will be increased in order to obtain the desired minimum thickness
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
     * 
     * Method for calculating the thickness of a terrain from a given position to an end point.
     * @param lowLine the low line
     * @param hightLine the hight Line
     * @param start the beginning of the zone where the calculation is desired
     * @param end the end of the area where the calculation is desired
     * @return the card thickness in the form of an integer
     */
    public static int averageThickness(final int[] lowLine, final int[] hightLine, final int start, final int end) { // epaisseur moyenne
        int somme = 0;
        for (int x = start; x < end; x++) {
            somme += (lowLine[x] - hightLine[x]);
        }
        return (int) somme / (end - start);
    }
}
