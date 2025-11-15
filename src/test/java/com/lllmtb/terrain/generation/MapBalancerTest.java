package com.lllmtb.terrain.generation;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MapBalancerTest {

    

    @Test
    void testAverageThicknessNormal() {
        int[] low = {1,2,3,4};
        int[] high = {5,6,7,8};
        int avg = MapBalancer.averageThickness(low, high, 0, 4);
        assertEquals(4, avg);
    }

   
    @Test
    void testThicknessBalencerNoChange() {
        int[] low = {5,6,7};
        MapBalancer.thicknessBalencer(3,5,low);
        assertArrayEquals(new int[]{5,6,7}, low);
    }

    @Test
    void testThicknessBalencerReduceLowLine() {
        int[] low = {10,15,20};
        MapBalancer.thicknessBalencer(8,5,low);
        assertArrayEquals(new int[]{7,12,17}, low);
    }

    @Test
    void testThicknessBalencerCannotReduceBelowZero() {
        int[] low = {2,1,5};
        MapBalancer.thicknessBalencer(5,3,low);
        assertArrayEquals(new int[]{2,1,3}, low);
    }

    @Test
    void testLeftThickerIncreaseHighLine() {
        int[] low = {1,1,1,1};
        int[] high = {10,10,2,2};
        int height = 10;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(2, high[2]);
        assertEquals(2, high[3]);
        assertEquals(1, low[2]);
        assertEquals(1, low[3]);
    }

@Test
void testLeftThickerReduceLowLineBranch() {
    
    int[] low = {5, 5, 25, 25};      
    int[] high = {10, 10, 8, 8};      
    int height = 15;                  



    MapBalancer.leftRightBalancing(low, high, height);

    // Vérification
    assertEquals(5, low[0]);
    assertEquals(5, low[1]);
    assertEquals(3, low[2]);  // 25 - 22 = 3
    assertEquals(3, low[3]);  // 25 - 22 = 3
}

    @Test
    void testLeftThickerCannotReduceLowLine() {
        int[] low = {2,3,5,5};
        int[] high = {20,20,10,10};
        int height = 15;
        MapBalancer.leftRightBalancing(low, high, height);
      
        assertEquals(5, low[2]);
        assertEquals(5, low[3]);
    }


    @Test
    void testRightThickerIncreaseHighLine() {
        int[] low = {1,1,1,1};
        int[] high = {2,2,5,5};
        int height = 10;
        MapBalancer.leftRightBalancing(low, high, height);
        assertArrayEquals(new int[]{5,5,5,5}, high);
        assertArrayEquals(new int[]{1,1,1,1}, low);
    }

    
    @Test
    void testRightThickerReduceLowLine() {
        int[] low = {10,10,15,15};
        int[] high = {0,0,10,10};
        int height = 20; 
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(10, low[0]);
        assertEquals(10, low[1]);
    }

    
    @Test
    void testRightThickerCannotReduceLowLine() {
        int[] low = {5,5,1,1};
        int[] high = {0,0,10,10};
        int height = 10;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(5, low[0]);
        assertEquals(5, low[1]);
    }

   
    @Test
    void testLeftRightBalancedNoChange() {
        int[] low = {1,1,1,1};
        int[] high = {3,3,3,3};
        int height = 10;
        MapBalancer.leftRightBalancing(low, high, height);
        assertArrayEquals(new int[]{3,3,3,3}, high);
        assertArrayEquals(new int[]{1,1,1,1}, low);
    }

    @Test
    void testConstructeur(){
        new MapBalancer(); //test inutile mais permet de couvrir la ligne declarant la classe;
    }
}
