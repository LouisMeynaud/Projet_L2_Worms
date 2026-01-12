package com.lllmtb.terrain.generation;
import com.lllmtb.engine.logger.Logger;

/**
 * A class that allows for map balancing after random generation in order to
 * maintain consistency and fairness between the two sides and therefore the two
 * teams.
 */
public class MapBalancer {

    /**
     *
     * increases/decreases the thickness of the left/right part of lowLine/heightLine in order to maintain fairness between the left and right team.
     * @param lowLine
     * @param hightLine
     * @param height    the height of the map (allows highLine not to exceed it);
     *
     */
    protected static void leftRightBalancing(final int[] lowLine, final int[] hightLine, final int height) {
        try {
            final int width = hightLine.length;
            final int mid = width / 2;
            final int diffAccepted = 3;

            final int leftAverage = averageThickness(lowLine, hightLine, 0, mid);
            final int rightAverage = averageThickness(lowLine, hightLine, mid, width);
            final int diff = leftAverage - rightAverage;

            if (diff >= diffAccepted) {
                // Parameter to change: the larger the difference, the less likely the terrain is to be balanced.
                // Left side thicker, balance by increasing the right side.
                for (int x = mid; x < width; x++) {
                    double ratio = (double) (x - mid) / (width - mid);  // permet d'evité d'avoir un grand mur au milieu ,plus on se rapproche du centre , moin on ajoute de bloc.
                    int adjust = (int) (diff * ratio);
                    if (hightLine[x] + adjust < height) {
                        hightLine[x] += adjust;
                    } else if (lowLine[x] > 0) {
                        lowLine[x] = Math.max(0, lowLine[x] - adjust);
                    }
                }
            } else if (diff <= -diffAccepted) {
                //Thicker right side, balance by increasing the left side
                int absDiff = -diff;
                for (int x = 0; x < mid; x++) {
                    double ratio = 1.0 - ((double) x / mid);
                    int adjust = (int) (absDiff * ratio);
                    if (hightLine[x] + adjust < height) {
                        hightLine[x] += adjust;
                    } else if (lowLine[x] > 0) {
                        lowLine[x] = Math.max(0, lowLine[x] - adjust);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logger.error("Erreur dans leftRightBalancing", e);
        }
    }

    /**
     * method to avoid excessively thin maps by introducing a minimum thickness.
     * @param thicknessMin the minimum desired thickness
     * @param epaisseur    the current thickness of the card
     * @param hightLine    the upper line which will be increased in order to obtain the desired minimum thickness
     * @param mapHeight    the maximum height (the height of the map)
     */
    protected static void thicknessBalencer(final int thicknessMin, final int epaisseur, final int[] hightLine, int mapHeight) {
        try {
            if (thicknessMin > epaisseur) {
                int toAdd = thicknessMin - epaisseur;
                for (int i = 0; i < hightLine.length; i++) {
                    hightLine[i] += toAdd;
                    if (hightLine[i] >= mapHeight) {
                        hightLine[i] = mapHeight - 1;
                    }
                }
            }
        } catch (Exception e) {
            Logger.logger.error("Erreur dans thicknessBalencer", e);
        }
    }

    /**
     *
     * Method for calculating the thickness of a terrain from a given position to an end point.
     * @param lowLine   the low line
     * @param hightLine the hight Line
     * @param start     the beginning of the zone where the calculation is desired
     * @param end       the end of the area where the calculation is desired
     * @return the card thickness in the form of an integer
     */
    protected static int averageThickness(final int[] lowLine, final int[] hightLine, final int start, final int end) { // epaisseur moyenne
        try {
            int somme = 0;
            if (end <= start) return 0;
            for (int x = start; x < end; x++) {
                somme += (hightLine[x] - lowLine[x]);
            }
            return somme / (end - start);
        } catch (Exception e) {
            Logger.logger.error("Error in averageThickness", e);
            return 0;
        }
    }
}
