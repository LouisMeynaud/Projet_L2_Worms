package com.lllmtb.terrain.generation;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.SaveMap;


public class GenerateMap {
    public static ConfigMap world = new ConfigMap(100, 4, 5, false, 17, LandScape.HILLY, 1, 4,GameMode.NORMAL);
    public static ConfigMap flyingWorld = new ConfigMap(100, 4, 5, true, 17, LandScape.HILLY, 1, 4,GameMode.NORMAL);


    /**
     * method for generating a random map
     * @param config takes a config type object in order to generate a map according to presets
     * @return the create map
     */
    public static Map createMap(ConfigMap config) {
        Map map = new Map(config.getHeight(), config.getWidth(), SaveMap.newMapSaveFile());
        int[] lowLine;

        int[] hightLine = LineGenerator.createLine(config);
        if (config.isFlyingIsland()) {
            lowLine = LineGenerator.createLine(config);
            triHightLineLowLine(hightLine, lowLine, config);

        } else {
            lowLine = new int[config.getWidth()];
            for (int i = 0; i < config.getWidth(); i++) {
                lowLine[i] = config.getWaterHeight() + 1;
            }
        }


        MapBalancer.leftRightBalancing(lowLine, hightLine, config.getHeight());
        MapBalancer.thicknessBalencer(config.getThicknessMin(), MapBalancer.averageThickness(lowLine, hightLine, 0, config.getWidth()), hightLine);
        MapFiller.fillingWater(map, config.getWaterHeight());
        MapFiller.fillingDirt(map, hightLine, lowLine,config);

        return map;
    }

    /**
     * methode pour que la hightLine soit toujours au dessus de la lowLine
     * @param hightLine la ligne haute
     * @param lowLine   la ligne basse
     * @param config    la config de la map
     */
    protected static void triHightLineLowLine(int[] hightLine, int[] lowLine, ConfigMap config) {
        for (int i = 0; i < config.getWidth(); i++) {
            if (lowLine[i] > hightLine[i]) {
                int temp = lowLine[i];
                lowLine[i] = hightLine[i];
                hightLine[i] = temp;
            }
        }
    }
    // attention probleme, le right balence crée une pente enorme au mileu

}