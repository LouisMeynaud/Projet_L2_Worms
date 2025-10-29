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

        int[] ligneHaute = MapGenerator.creationligne(width, height, pentemax, longeurmax);
        int[] ligneBasse = MapGenerator.creationligne(width, height, pentemax, longeurmax);

        for (int i = 0; i < width; i++) {
            if (ligneBasse[i] > ligneHaute[i]) {
                int temp = ligneBasse[i];
                ligneBasse[i] = ligneHaute[i];
                ligneHaute[i] = temp;
            }
        }
        MapBalancer.equilibrageGaucheDroite(ligneBasse, ligneHaute, width, height);
        MapFiller.remplissageDirt(map, ligneHaute, ligneBasse);
        MapFiller.remplissageWater(map, heightWater);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(createMatrix(120, 60, 3, 5, 4));
    }

}