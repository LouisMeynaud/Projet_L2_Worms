package com.lllmtb.terrain.generation;

import com.lllmtb.terrain.map.*;
import com.lllmtb.physics.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MapFillerTest {
    @Test
    void terrainCorrect() {
        Map map = new Map(10, 10, "test");
        int[] ligneBasse = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] ligneHaute = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        MapFiller.fillingDirt(map, ligneHaute, ligneBasse);

        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                position = map.cooMapToCooMatrix(position);
                if (i < ligneHaute[j] || i > ligneBasse[j]) {
                   assertEquals(Tile.AIR, map.getTile(position));
                } else {
                    assertEquals(Tile.DIRT, map.getTile(position));
                }
            }
        }
    }

    @Test
    void eauCorrecte() {
        Map map = new Map(10, 10, "test");
        int[] ligneBasse = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] ligneHaute = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        MapFiller.fillingWater(map, 3);
        MapFiller.fillingDirt(map, ligneHaute, ligneBasse);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y <= 3; y++) {
                Position pos = new Position(x, y);
                    assertEquals(Tile.WATER, map.getTile(pos));
            }
        }
    }
}
