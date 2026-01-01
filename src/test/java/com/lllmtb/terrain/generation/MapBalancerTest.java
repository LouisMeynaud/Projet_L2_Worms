package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testAverageThicknessZeroRange() {
        int[] low = {1};
        int[] high = {5};
        int avg = MapBalancer.averageThickness(low, high, 0, 0);
        assertEquals(0, avg);
    }
   
    @Test
    void testThicknessBalencerNoChange() {
        int[] high = {10, 11, 12};
        int mapHeight = 20;
        MapBalancer.thicknessBalencer(3, 5, high, mapHeight);
        assertArrayEquals(new int[]{10, 11, 12}, high);
    }

    @Test
    void testThicknessBalencerIncreaseHighLine() {
        int[] high = {10,15,20};
        int mapHeight = 100;
        MapBalancer.thicknessBalencer(8, 5, high, mapHeight);
        assertArrayEquals(new int[]{13, 18, 23}, high);
    }

    @Test
    void testThicknessBalencerCapAtMapHeight() {
        int[] high = {90, 95, 98};
        int mapHeight = 100;
        MapBalancer.thicknessBalencer(25, 10, high, mapHeight);
        assertArrayEquals(new int[]{99, 99, 99}, high);
    }

    @Test
    void testThicknessBalencerEmptyArray() {
        int[] high = {};
        MapBalancer.thicknessBalencer(10, 5, high, 20);
        assertArrayEquals(new int[]{}, high);
    }

    @Test
    void testLeftThickerIncreaseHighLine() {
        int[] low = {1,1,1,1};
        int[] high = {10,10,2,2};
        int height = 50;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(2, high[2]);
        assertEquals(6, high[3]); 
    }

@Test
void testLeftThickerReduceLowLineBranch() {
    
    int[] low = {5, 5, 25, 25};      
    int[] high = {10, 10, 8, 8};      
    int height = 15;                  



    MapBalancer.leftRightBalancing(low, high, height);

    assertEquals(25, low[2]);
    assertEquals(14, low[3]);
}

    @Test
    void testLeftThickerCannotReduceLowLine() {
        int[] low = {0, 0, 0, 0};
        int[] high = {20, 20, 10, 10}; 
        int height = 10;
        MapBalancer.leftRightBalancing(low, high, height);
      
        assertEquals(0, low[3]);
        assertEquals(10, high[3]);
    }


    @Test
    void testRightThickerIncreaseHighLine() {
        int[] low = {1, 1, 1, 1};
        int[] high = {2, 2, 10, 10};
        int height = 50;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(10, high[0]);
        assertEquals(6, high[1]);
    }

    
    @Test
    void testRightThickerReduceLowLine() {
        int[] low = {20, 20, 0, 0};
        int[] high = {5, 5, 25, 25};
        int height = 5;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(0, low[0]);
    }
    @Test
    void testRightThickerCannotReduceLowLine() {
        int[] low = {0, 0, 0, 0};
        int[] high = {5, 5, 20, 20};
        int height = 5;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(0, low[0]);
        assertEquals(5, high[0]);
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
    void testLeftRightBalancingTinyMap() {
        int[] low = {0};
        int[] high = {100};
        int height = 200;
        MapBalancer.leftRightBalancing(low, high, height);
        assertEquals(100, high[0]);
    }

    @Test
    void testConstructeur(){
        new MapBalancer(); //test inutile mais permet de couvrir la ligne declarant la classe;
    }
}
