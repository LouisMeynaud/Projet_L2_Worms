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

    public static Map createMatrix(int width, int height, int pentemax, int longeurmax, int heightWater) {
        Map map = new Map(height, width, SaveMap.newMapSaveFile());

        int[] tallLine = MapGenerator.createLine(width, height, pentemax, longeurmax);
        int[] lowLine = MapGenerator.createLine(width, height, pentemax, longeurmax);

        for (int i = 0; i < width; i++) {
            if (lowLine[i] > tallLine[i]) {
                int temp = lowLine[i];
                lowLine[i] = tallLine[i];
                tallLine[i] = temp;
            }
        }
        MapBalancer.leftRightBalancing(lowLine, tallLine, width, height);
        MapFiller.fillingDirt(map, tallLine, lowLine);
        MapFiller.fillingWater(map, heightWater);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(createMatrix(120, 60, 3, 5, 4));
    }

}