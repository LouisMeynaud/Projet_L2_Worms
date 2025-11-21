package com.lllmtb.terrain.generation;

import java.util.Random;

public class LineGenerator {

    protected static int[] createLine(ConfigMap config) {
        int lineHeight = config.heightStart;
        int x = 0;
        int slope = 0;
        int length = 0;
        int[] line = new int[config.width];
        Random random = new Random();
        while (x < config.width) {
            do {
                if (random.nextInt(101) < config.plateauChance) { 
                    slope = 0;
                } else {
                    slope = random.nextInt(config.maxSlope - (- config.maxSlope) + 1) + (- config.maxSlope);
                }
                length = random.nextInt(config.maxLength + 1);
            } while (lineHeight + slope * length >= config.height || lineHeight + slope * length <= 0);
            for (int i = 0; i < length && x < config.width; i++) {
                line[x] = lineHeight;
                lineHeight += slope;
                x++;
            }
        }
        return line;
    }
    
}
