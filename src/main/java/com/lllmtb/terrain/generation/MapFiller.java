package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.physics.*;

public class MapFiller {
    /**
     * methode permettant de remplir la map d'eau du sol a la hauteur souhaiter
     * @param map la map que l'on veut remplir
     * @param heightWater la huateur de l'eau souhaite
     */
    public static void fillingWater(Map map, int heightWater) {
        for (int i = 0; i <= heightWater; i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);

                map.setTile(position, Tile.WATER);

            }
        }
    }

    /**
     *  methode permettant de remplir la zone entre la ligne haute et basse de DIRT
     * @param map la map que l'on veut remplir
     * @param hightLine la ligne haute de la map
     * @param lowLine la ligne basse de la map
     */
    public static void fillingDirt(Map map, int[] hightLine, int[] lowLine) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                position = map.cooMapToCooMatrix(position);
                if ((i < hightLine[j] || i > lowLine[j])&& !map.isTileAt(position, Tile.WATER)) {
                    map.setTile(position, Tile.AIR);
                } else if(!map.isTileAt(position, Tile.WATER)){
                    map.setTile(position, Tile.DIRT);
                }
            }
        }
    }
}
