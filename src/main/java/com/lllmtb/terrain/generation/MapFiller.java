package com.lllmtb.terrain.generation;

import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

public class MapFiller {
    /**
     * method for filling the ground water map to the desired height
     * @param map         the map that we want to fill
     * @param heightWater the water height wishes
     */
    protected static void fillingWater(Map map, int heightWater) {
        for (int i = 0; i <= heightWater; i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                map.setTile(position, Tile.WATER);

            }
        }
    }

    /**
     * method for filling the area between the top and bottom lines of DIRT
     * @param map       the map that we want to fill
     * @param hightLine the top line of the map
     * @param lowLine   the bottom line of the map
     */
    protected static void fillingDirt(Map map, int[] hightLine, int[] lowLine) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                if ((i == hightLine[j] && i > lowLine[j]) && !map.isTileAt(position, Tile.WATER)) {
                    map.setTile(position, Tile.GRASS);
                } else if ((i < hightLine[j] && i == lowLine[j]) && !map.isTileAt(position, Tile.WATER)) {
                    map.setTile(position, Tile.UNDERDIRT);
                } else if ((i < hightLine[j] && i > lowLine[j]) && !map.isTileAt(position, Tile.WATER)) {
                    map.setTile(position, Tile.DIRT);
                } else if (!map.isTileAt(position, Tile.WATER)) {
                    map.setTile(position, Tile.AIR);
                }
            }
        }
    }
}
