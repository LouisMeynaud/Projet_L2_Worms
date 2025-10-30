package com.lllmtb.terrain.generation;

import java.util.Random;

public class MapGenerator {
    public static int[] createLine(int width, int height, int slopeMax, int lengthMax) {
        int LineHeight = height / 5;
        int x = 0;
        int slope = 0;
        int length = 0;
        int[] line = new int[width];
        Random random = new Random();
        while (x < width) {
            do {
                if (random.nextInt(101) < 40) { // 40% de chance d'avoir un plateau , parametre modifiable par la suite
                    slope = 0;
                } else {
                    slope = random.nextInt(slopeMax - (-slopeMax) + 1) + (-slopeMax);
                }
                length = random.nextInt(lengthMax + 1);
            } while (LineHeight + slope * length >= height || LineHeight + slope * length < 0);
            for (int i = 0; i < length && x < width; i++) {
                line[x] = LineHeight;
                LineHeight += slope;
                x++;
            }
        }
        return line;
    }
    
}
