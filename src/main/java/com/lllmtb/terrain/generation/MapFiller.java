package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.physics.*;

public class MapFiller {
    public static void fillingWater(Map map, int heightWater) {
        for (int i = 0; i <= heightWater; i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);

                map.setTile(position, Tile.WATER);

            }
        }
    }

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
