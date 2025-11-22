package com.lllmtb.terrain.generation;

import java.util.Random;

public class LineGenerator {

    protected static int[] createLine(ConfigMap config) {
        int LineHeight = config.getHeightStart();
        int x = 0;
        int slope = 0;
        int length = 0;
        int[] line = new int[config.getWidth()];
        Random random = new Random();
        while (x < config.getWidth()) {
            do {
                if (random.nextInt(101) < config.getPlateauChance()) { 
                    slope = 0;
                } else {
                    slope = random.nextInt(config.getMaxSlope() - (- config.getMaxSlope()) + 1) + (- config.getMaxSlope());
                }
                length = random.nextInt(config.getMaxLength() + 1);
            } while (LineHeight + slope * length >= config.getHeight() || LineHeight + slope * length <= 0);
            for (int i = 0; i < length && x < config.getWidth(); i++) {
                line[x] = LineHeight;
                LineHeight += slope;
                x++;
            }
        }
        return line;
    }
    
}
