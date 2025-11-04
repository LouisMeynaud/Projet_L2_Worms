package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class mapGeneratorTest {
    @Test
    void correctlength() {
        int width = 50;
        int[] ligne = MapGenerator.createLine(width, 20, 3, 5);
        assertEquals(width, ligne.length);
    }

    @Test
    void heightMaxAndMinRespected() {
        int height = 20;
        int[] ligne = MapGenerator.createLine(50, height, 3, 5);
        for (int h : ligne) {
            assertTrue(h >= 0 && h < height);
        }
    }
}
