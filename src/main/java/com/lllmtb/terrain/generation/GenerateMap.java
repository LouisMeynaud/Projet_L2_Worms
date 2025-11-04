package com.lllmtb.terrain.generation;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.SaveMap;

// note tempo 
//pente + forte = plus chaotique
//longueur plus haute = terrain plus valoner
//mieux gerer les parametre par la suite.
//le code sera changer

//verification a faire lors de la creation de la map ; espace de terrain remplie ; meme quantité de terre des deux coter , crée un moyen de faire des petite 'ile' sans que sa soit le bordel
public class GenerateMap {
    static ConfigMap WORLD = new ConfigMap(370, 170, 2, 3, 8, 4, false,170-25);
    static ConfigMap FlYINGWORD = new ConfigMap(370, 170, 2, 3, 8, 4, true,170-50);
    //static ConfigMap CHAOSWORLD = new ConfigMap(370, 170, 3, 7, 8, 6, false,170-25);

    public static Map createMatrix(ConfigMap config) {
        Map map = new Map(config.height, config.width, SaveMap.newMapSaveFile());
        int[] lowLine;

        int[] hightLine = MapGenerator.createLine(config.width, config.height, config.maxSlope, config.maxLength,config.heightStart-20); // has smaller values than lowLine (because 0 is at the top)
        if(config.flyingIsland){
        lowLine = MapGenerator.createLine(config.width, config.height, config.maxSlope, config.maxLength,config.heightStart); // has larger values than hightLine (because 0 is at the top)
        for (int i = 0; i < config.width; i++) {
            if (lowLine[i] < hightLine[i]) {
                int temp = lowLine[i];
                lowLine[i] = hightLine[i];
                hightLine[i] = temp;
            }
        }}
        else{
            lowLine = new int[config.width];
         for (int i = 0; i < config.width; i++) {
             lowLine[i] =config.height-1;
            }
        }

        MapBalancer.leftRightBalancing(lowLine, hightLine, config.height);
        MapBalancer.thicknessBalencer(config.thicknessMin, MapBalancer.averageThickness(lowLine, hightLine, 0, config.width), hightLine);
        MapFiller.fillingWater(map, config.waterHeight);
        MapFiller.fillingDirt(map, hightLine, lowLine);
        
        return map;
    }

        
        //attention probleme, le right balence crée une pente enorme au mileu
    

}