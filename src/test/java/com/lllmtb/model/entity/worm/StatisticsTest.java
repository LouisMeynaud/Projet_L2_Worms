package com.lllmtb.model.entity.worm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class StatisticsTest {
    Statistics stats;

    
    @BeforeEach
    public void setup(){
        stats = new Statistics(100, 1, 1);

    }


    @Test
    public void testGetHp(){
        assertEquals(100, stats.getHp());
    }

    @Test
    public void testGetSpeed(){
        assertEquals(1, stats.getSpeed());
    }

    @Test
    public void testGetStrength(){
        assertEquals(1, stats.getStrength());
    }

    @Test
    public void testSetHp(){
        stats.setHp(10);
        assertEquals(10, stats.getHp());
    }
    @Test
    public void testSetSpeed(){
        stats.setSpeed(10);
        assertEquals(10, stats.getSpeed());
    }
    @Test
    public void testSetStrength(){
        stats.setStrength(10);
        assertEquals(10, stats.getStrength());
    }

        @Test
    public void testSetHpError(){
        stats.setHp(-1);
        assertEquals(0, stats.getHp());
    }

    @Test
    public void testSetSpeedError(){
        stats.setSpeed(-1);
        assertEquals(1, stats.getSpeed());
    }

    @Test
    public void testSetStrengthError(){
        stats.setStrength(-1);
        assertEquals(1, stats.getStrength());
    }
    @Test
    public void testIsAliveTrue(){
        assertTrue(stats.isAlive());
    }
    @Test
    public void testIsAliveFalse(){
        stats.setHp(0);
        assertFalse(stats.isAlive());
    }



    
}
