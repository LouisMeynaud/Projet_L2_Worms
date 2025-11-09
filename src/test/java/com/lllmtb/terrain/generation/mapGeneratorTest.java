package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class mapGeneratorTest {
    @Test
    void correctlength() {
        int width = 50;
        ConfigMap config = new ConfigMap(width, 20, 3, 5, 0, 0, false, 0, 1);
        int[] ligne = MapGenerator.createLine(config);
        assertEquals(width, ligne.length);
    }

    @Test
    void heightMaxAndMinRespected() {
        int height = 20;
         ConfigMap config = new ConfigMap(50, height, 3, 5, 0, 0, false, 0, 1);
        int[] ligne = MapGenerator.createLine(config);
        for (int h : ligne) {
            assertTrue(h >= 0 && h < height);
        }
    }
}
