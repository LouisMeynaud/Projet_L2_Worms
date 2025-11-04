package com.lllmtb.terrain.generation;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class MapBalancerTest {

    @Test
    void noModificationIfBalanced() {
        int[] lowLine = { 5, 5, 5, 5 };
        int[] highLine = { 10, 10, 10, 10 };
        int[] lowLineCopy = { 5, 5, 5, 5 };
        int[] highLineCopy = { 10, 10, 10, 10 };
        MapBalancer.leftRightBalancing(lowLine, highLine, 4, 20);
        assertArrayEquals(lowLineCopy, lowLine);
        assertArrayEquals(highLineCopy, highLine);
    }

     @Test
    void leftSideThickerCorrection() {
        int[] highLine = { 100, 100, 100, 100 };
        int[] lowLine = { 120, 120, 105, 105 };
        MapBalancer.leftRightBalancing(lowLine, highLine, 4, 300);
        assertTrue(highLine[3] < 100);}

    @Test
    void rightSideThickerCorrection() {
        int[] highLine = { 100, 100, 100, 100 };
        int[] lowLine = { 105, 105, 120, 120 };
        MapBalancer.leftRightBalancing(lowLine, highLine, 4, 300);
        assertTrue(highLine[0] < 100);
    }

    @Test
    void averageThicknessCalculation() {
        int[] highLine = { 1, 2, 1, 2 };
        int[] lowLine = { 5, 6, 5, 6 };
        double average = MapBalancer.averageThickness(lowLine, highLine, 0, 4);
        assertEquals(4.0, average);
    }

    @Test
    void rightSideThickerAndTallLineTooSmall() {
        int maxHeight = 100;
        int[] highLine = { 2, 2, 20, 20 };
        int[] lowLine = { 50, 50, 90, 90 }; // right thicker, left high should decrease but too small
        int width = 4;

        MapBalancer.leftRightBalancing(lowLine, highLine, width, maxHeight);
        System.out.println(Arrays.toString(highLine));
        System.out.println(Arrays.toString(lowLine));

        // check that the left low line has been increased
        assertTrue(lowLine[1] > 50);
    }


    @Test
    void leftSideThickerAndTallLineTooSmall() {
        int maxHeight = 100;
        int[] highLine = { 20, 20, 2, 2 };
        int[] lowLine = { 90, 90, 50, 50 }; // left thicker than right, right high should decrease but too small
        int width = 4;

        MapBalancer.leftRightBalancing(lowLine, highLine, width, maxHeight);
        // check that the right low line has been increased
        assertTrue(lowLine[3] > 50);
    }

      void rightSideThickerAndLowTooHighAndTallTooLow() {
        int maxHeight = 10;
        // left avg = 5, right avg = 8 , diff = 3,but nothing can change in position 0.
        int[] highLine = { 0, 5, 1, 1 };
        int[] lowLine = { 10, 5, 9, 9 };

        int width = 4;

        MapBalancer.leftRightBalancing(lowLine, highLine, width, maxHeight);
        System.out.println(Arrays.toString(highLine));
        System.out.println(Arrays.toString(lowLine));

        // verify that far-left column didn’t change
        assertTrue(highLine[0] == 0);
        assertTrue(lowLine[0] == 10);
    }
    
    @Test
    void leftSideThickerAndLowTooHighAndTallTooLow() {
        int maxHeight = 10;
        // left avg = 8, right avg = 5 , diff = 3 but nothing can change in position 3
        int[] highLine = { 1, 1, 5, 0 };
        int[] lowLine = { 9, 9, 5, 10 };

        int width = 4;

        MapBalancer.leftRightBalancing(lowLine, highLine, width, maxHeight);

        // verify that far-right column didn’t change
        assertTrue(highLine[3] == 0);
        assertTrue(lowLine[3] == 10);
    }

    @Test
    void classDeclarationTest() {
        new MapBalancer(); // useless since class is static, but covers declaration line
    }
}
