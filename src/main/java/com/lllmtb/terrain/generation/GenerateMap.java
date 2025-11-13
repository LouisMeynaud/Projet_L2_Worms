package com.lllmtb.terrain.generation;


import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.SaveMap;


public class GenerateMap {
    public static ConfigMap WORLD = new ConfigMap(370, 170, 2, 3, 8, 4, false, 20, 40);
    public static ConfigMap FLYINGWORLD = new ConfigMap(370, 170, 2, 3, 8, 4, true, 20, 40);
    

    /**
     * method for generating a random map
     * 
     * @param config takes a config type object in order to generate a map according to presets
     * @return the create map
     */
    public static Map createMap(ConfigMap config) {
        Map map = new Map(config.height, config.width, SaveMap.newMapSaveFile());
        int[] lowLine;

        int[] hightLine = LineGenerator.createLine(config); 
        if (config.flyingIsland) {
            lowLine = LineGenerator.createLine(config); 
            TriHightLineLowLine(hightLine,lowLine,config);
            
        } else {
            lowLine = new int[config.width];
            for (int i = 0; i < config.width; i++) {
                lowLine[i] = 0;
            }
        }

        MapBalancer.leftRightBalancing(lowLine, hightLine, config.height);
        MapBalancer.thicknessBalencer(config.thicknessMin,MapBalancer.averageThickness(lowLine, hightLine, 0, config.width), hightLine);
        MapFiller.fillingWater(map, config.waterHeight);
        MapFiller.fillingDirt(map, hightLine, lowLine);

        return map;
    }
    
     /**
     * methode pour que la hightLine soit toujours au dessus de la lowLine
     * 
     * @param highLine la ligne haute
     * @param lowLine la ligne basse
     * @param config la config de la map
     */
    protected  static void TriHightLineLowLine(int[] hightLine,int[] lowLine,ConfigMap config){
        for (int i = 0; i < config.width; i++) {
                if (lowLine[i] > hightLine[i]) {
                    int temp = lowLine[i];
                    lowLine[i] = hightLine[i];
                    hightLine[i] = temp;
                }
            }
    }
    // attention probleme, le right balence crée une pente enorme au mileu

}