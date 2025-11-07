package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.SaveMap;

public class GenerateMap {
    static ConfigMap WORLD = new ConfigMap(370, 170, 2, 3, 8, 4, false, 170 - 25, 40);
    static ConfigMap FlYINGWORLD = new ConfigMap(370, 170, 2, 3, 8, 4, true, 170 - 50, 40);
    

    /**
     * methode permettant la generation d'une map aleatoire
     * 
     * @param config prend un objet de type config afin de generer une map suivant
     *               des presete
     * @return la map generer
     */
    public static Map createMap(ConfigMap config) {
        Map map = new Map(config.height, config.width, SaveMap.newMapSaveFile());
        int[] lowLine;

        int[] hightLine = MapGenerator.createLine(config); // has smaller values than lowLine (because 0 is at the top)
        if (config.flyingIsland) {
            lowLine = MapGenerator.createLine(config); // has larger values than hightLine (because 0 is at the top)
            for (int i = 0; i < config.width; i++) {
                if (lowLine[i] < hightLine[i]) {
                    int temp = lowLine[i];
                    lowLine[i] = hightLine[i];
                    hightLine[i] = temp;
                }
            }
        } else {
            lowLine = new int[config.width];
            for (int i = 0; i < config.width; i++) {
                lowLine[i] = config.height - 1;
            }
        }

        MapBalancer.leftRightBalancing(lowLine, hightLine, config.height);
        MapBalancer.thicknessBalencer(config.thicknessMin,
                MapBalancer.averageThickness(lowLine, hightLine, 0, config.width), hightLine);
        MapFiller.fillingWater(map, config.waterHeight);
        MapFiller.fillingDirt(map, hightLine, lowLine);

        return map;
    }

    // attention probleme, le right balence crée une pente enorme au mileu

}