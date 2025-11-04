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

    public static Map createMatrix(int width, int height, int maxSlope, int maxLength, int waterHeight) {
        Map map = new Map(height, width, SaveMap.newMapSaveFile());

        int[] topLine = MapGenerator.createLine(width, height, maxSlope, maxLength); // has smaller values than bottomLine (because 0 is at the top)
        int[] bottomLine = MapGenerator.createLine(width, height, maxSlope, maxLength); // has larger values than topLine (because 0 is at the top)

        for (int i = 0; i < width; i++) {
            if (bottomLine[i] < topLine[i]) {
                int temp = bottomLine[i];
                bottomLine[i] = topLine[i];
                topLine[i] = temp;
            }
        }

        MapBalancer.leftRightBalancing(bottomLine, topLine, width, height);
        MapFiller.fillingDirt(map, topLine, bottomLine);
        MapFiller.fillingWater(map, waterHeight);
        return map;
    }

}