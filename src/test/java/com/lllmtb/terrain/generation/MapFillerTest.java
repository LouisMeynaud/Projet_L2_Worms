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

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Position pos = new Position(x, y);
                if (y < ligneHaute[x] || y > ligneBasse[x]) {
                    assertEquals(Tile.AIR, map.getTile(pos));
                } else {
                    assertEquals(Tile.DIRT, map.getTile(pos));
                }
            }
        }
    }

    @Test
    void eauCorrecte() {
        Map map = new Map(10, 10, "test");
        int[] ligneBasse = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] ligneHaute = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        MapFiller.fillingDirt(map, ligneHaute, ligneBasse);

        MapFiller.fillingWater(map, 3);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y <= 3; y++) {
                Position pos = new Position(x, y);
                if (map.getTile(pos) != Tile.DIRT) {
                    assertEquals(Tile.WATER, map.getTile(pos));
                }
            }
        }
    }
}
