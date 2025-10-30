package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.generation.MapBalancer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class MapBalancerTest {

    @Test
    void pasDeModificationSiEquilibre() {
        int[] bas = { 5, 5, 5, 5 };
        int[] haut = { 10, 10, 10, 10 };
        int[] basBis = { 5, 5, 5, 5 };
        int[] hautBis = { 10, 10, 10, 10 };
        MapBalancer.leftRightBalancing(bas, haut, 4, 20);
        assertArrayEquals(basBis, bas);
        assertArrayEquals(hautBis, haut);
    }

     @Test
    void correctiongauchePlusEpaisse() {
        int[] ligneHaut = { 100, 100, 100, 100 };
        int[] ligneBas = { 120, 120, 105, 105 };
        MapBalancer.leftRightBalancing(ligneBas, ligneHaut, 4, 300);
        System.out.println(Arrays.toString(ligneBas));
        System.out.println(Arrays.toString(ligneHaut));
        assertTrue(ligneHaut[3]<100);}

    @Test
    void correctiondroitePlusEpaisse() {
        int[] ligneHaut = { 100, 100, 100, 100 };
        int[] ligneBas = { 105, 105, 120, 120 };
        MapBalancer.leftRightBalancing(ligneBas, ligneHaut, 4, 300);
        System.out.println(Arrays.toString(ligneBas));
        System.out.println(Arrays.toString(ligneHaut));
        assertTrue(ligneHaut[0]<100);
                                                                                                            
    }

    @Test
    void moyenneEpaisseurCalcul() throws Exception {
        int[] haut = { 1, 2, 1, 2 };
        int[] bas = { 5, 6, 5, 6 };
        double moyenne = MapBalancer.averageThickness(bas, haut, 0, 4);
        assertEquals(4.0, moyenne);
    }

    @Test
    void correctionDroitePlusEpaisseEtTallTropPetit() {
        int hauteurMax = 100;
        int[] tallLine = { 2, 2, 20, 20 }; 
        int[] lowLine = { 50,50, 90, 90 }; // droite plus epaisse donc tall gauche doit diminuer mais trop petit
        int largeur = 4;

        MapBalancer.leftRightBalancing(lowLine, tallLine, largeur, hauteurMax);
        System.out.println(Arrays.toString(tallLine));
        System.out.println(Arrays.toString(lowLine));

        // vérifie que la ligne basse gauche a été augmentée
        assertTrue(lowLine[1] > 50);
    }

    @Test
    void correctionGauchePlusEpaisseEtLowTropHaute() {
        int hauteurMax = 100;
        int[] tallLine = { 20,20, 2, 2 };
        int[] lowLine = { 90, 90, 50, 50 }; // guache plus épaisse que droite donc tall droite doit etre diminuer mais trop petit
        int largeur = 4;

        MapBalancer.leftRightBalancing(lowLine, tallLine, largeur, hauteurMax);
        System.out.println(Arrays.toString(lowLine));
        System.out.println(Arrays.toString(tallLine));
        // vérifie que la ligne basse droite a été augmentée
        assertTrue(lowLine[3] > 50 );
    }

    @Test
    void correctionDroitePlusEpaisseEtLowTropHauteEtTallTropBasse() {
        int hauteurMax = 10;
        //moyenne gauche gauche = 5, moyenne droite = 8 donc diff = 3 or 0-3 < 0 et 10+3>0 donc la premiere case ne devrais pas etre modifier
        int[] tallLine = { 0, 5, 1,1 };
        int[] lowLine = { 10,5, 9, 9 }; 
        
        int largeur = 4;

        MapBalancer.leftRightBalancing(lowLine, tallLine, largeur, hauteurMax);
        System.out.println(Arrays.toString(tallLine));
        System.out.println(Arrays.toString(lowLine));

        // vérifie que la colonne tout a gauche n'a pas ete changer
        assertTrue(tallLine[0] == 0);
        assertTrue(lowLine[0] == 10);
        }
    
    @Test
   void correctionGauchePlusEpaisseEtLowTropHauteEtTallTropBasse() {
        int hauteurMax = 10;
        //moyenne gauche gauche = 8, moyenne droite = 5 donc diff = 3 or 0-3 < 0 et 10+3>0 donc la premiere case ne devrais pas etre modifier
        int[] tallLine = { 1,1,5,0};
        int[] lowLine = { 9,9,5,10}; 
        
        int largeur = 4;

        MapBalancer.leftRightBalancing(lowLine, tallLine, largeur, hauteurMax);
        System.out.println(Arrays.toString(tallLine));
        System.out.println(Arrays.toString(lowLine));

        // vérifie que la ccolonne tout a droite  n'a pas changer
        assertTrue(tallLine[3] == 0);
        assertTrue(lowLine[3] == 10);
        }

    @Test
    void testdeclaClass(){
        new MapBalancer(); // ligne inutile car classe static , mais permet la couverture de la ligne de declaration de la classe.
    }
}
