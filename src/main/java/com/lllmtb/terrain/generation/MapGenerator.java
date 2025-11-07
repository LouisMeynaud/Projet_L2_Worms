package com.lllmtb.terrain.generation;

import java.util.Random;

public class MapGenerator {
/**
 * 
 * @param width la longeur de la map
 * @param height la hauteur de la map (permet au sol de ne pas depasser cette hauteur)
 * @param slopeMax la difference de hauteur max entre deux coordonée x consecutif
 * @param lengthMax la longueur max durant laquelle la hauteur de chaque bloc consecutif vas augmenter de lengthMax
 * @param heightStart la hauteur de depart du sol souhaiter
 * @param plateauChance la chance que l'on est du sol plat (permet d'eviter les terrain uniquement composer de "pic")
 * @return la ligne correspondant a la hauteur du sol y a une coordonée x correspondant a la i'eme case du tableau.
 */
    public static int[] createLine(ConfigMap config) {
        int LineHeight = config.heightStart;
        int x = 0;
        int slope = 0;
        int length = 0;
        int[] line = new int[config.width];
        Random random = new Random();
        while (x < config.width) {
            do {
                if (random.nextInt(101) < config.plateauChance) { // 40% de chance d'avoir un plateau , parametre modifiable par la suite
                    slope = 0;
                } else {
                    slope = random.nextInt(config.maxSlope - (- config.maxSlope) + 1) + (- config.maxSlope);
                }
                length = random.nextInt(config.maxSlope + 1);
            } while (LineHeight + slope * length >= config.height || LineHeight + slope * length < 0);
            for (int i = 0; i < length && x < config.width; i++) {
                line[x] = LineHeight;
                LineHeight += slope;
                x++;
            }
        }
        return line;
    }
    
}
