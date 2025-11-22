package com.lllmtb.terrain.generation;
import  com.lllmtb.terrain.generation.LineGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LineGeneratorTest {
    @Test
    void correctlength() {
        int width = 50;
        ConfigMap config = new ConfigMap(width, 5, 5, false, 0, LandScape.HILLY,0,0);
        int[] ligne = LineGenerator.createLine(config);
        assertEquals(width, ligne.length);
    }

    @Test
    void heightMaxAndMinRespected() {
        int width = 50;
        int height = (int) (50 * ConfigMap.RATIOWIDTHHEIGHT);
         ConfigMap config = new ConfigMap(50, 5, 5, false, 0, LandScape.HILLY,0,0);
        int[] ligne = LineGenerator.createLine(config);
        for (int h : ligne) {
            assertTrue(h >= 0 && h < height);
        }
    }
}
