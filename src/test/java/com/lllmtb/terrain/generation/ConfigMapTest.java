package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.GameMode;

public class ConfigMapTest {
    


    @Test
    public void configtest() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.HILLY,GameMode.NORMAL);
        
        assertEquals(60, configtest.getWidth());
    }

    

        @Test
    public void configtestFLAT() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.FLAT,GameMode.NORMAL);
        
        assertEquals(1, configtest.getMaxSlope());
        assertEquals(10, configtest.getMaxLength());
        assertEquals(80, configtest.getPlateauChance());
    }

    @Test
    public void configtestHILLY() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.HILLY,GameMode.NORMAL);
        
        assertEquals(2, configtest.getMaxSlope());
        assertEquals(3, configtest.getMaxLength());
        assertEquals(30, configtest.getPlateauChance());
    }

    @Test
    public void configtestJAGGED() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.JAGGED,GameMode.NORMAL);
        
        assertEquals(3, configtest.getMaxSlope());
        assertEquals(5, configtest.getMaxLength());
        assertEquals(10, configtest.getPlateauChance());
    }

    @Test
    public void configtestCHAOTIC() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.CHAOTIC,GameMode.NORMAL);
        
        assertEquals(4, configtest.getMaxSlope());
        assertEquals(2, configtest.getMaxLength());
        assertEquals(0, configtest.getPlateauChance());
    }

    @Test
    public void configtestDEFAULT() {
       
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 50, LandScape.UNKNOWN, GameMode.NORMAL);
        
        assertEquals(2, configtest.getMaxSlope());
        assertEquals(3, configtest.getMaxLength());
        assertEquals(30, configtest.getPlateauChance());
    }
}
