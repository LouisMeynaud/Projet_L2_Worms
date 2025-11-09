package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.physics.*;

public class MapFiller {
    /**
     * method for filling the ground water map to the desired height
     * @param map the map that we want to fill
     * @param heightWater the water height wishes
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
     *  method for filling the area between the top and bottom lines of DIRT
     * @param map the map that we want to fill
     * @param hightLine the top line of the map
     * @param lowLine the bottom line of the map
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
