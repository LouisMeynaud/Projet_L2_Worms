package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

public class MapFillerTest {
    @Test
    void terrainCorrect() {
        Map map = new Map(10, 10, "test");
        int[] ligneHaute = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] ligneBasse = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        ConfigMap config = new ConfigMap(10, 4, 5, false, 17, LandScape.FLAT, GameMode.NORMAL);
        MapFiller.fillingDirt(map, ligneHaute, ligneBasse,config);

        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Position position = new Position(j, i);
                if (i < ligneHaute[j] && i > ligneBasse[j]) {
                   assertEquals(Tile.DIRT, map.getTile(position));
                } else if (i == ligneHaute[j] && i > ligneBasse[j]) {
                   assertEquals(Tile.GRASS, map.getTile(position));
                } else if (i < ligneHaute[j] && i == ligneBasse[j]) {
                   assertEquals(Tile.UNDERDIRT, map.getTile(position));
                } else {
                    assertEquals(Tile.AIR, map.getTile(position));
                }
            }
        }
    }

    @Test
    void eauCorrecte() {
        Map map = new Map(10, 10, "test");
        int[] ligneHaute = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int[] ligneBasse = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
        MapFiller.fillingWater(map, 3);
        ConfigMap config = new ConfigMap(10, 4, 5, false, 17, LandScape.FLAT,GameMode.NORMAL);
        MapFiller.fillingDirt(map, ligneHaute, ligneBasse,config);

        for (int y = 0; y <= 3; y++) {
            for (int x = 0; x <10; x++) {
                Position pos = new Position(x, y);
                    assertEquals(Tile.WATER, map.getTile(pos));
            }
        }
    }
    @Test
    void declaMapFiller(){
        new MapFiller(); // inutile mais permet de couvrir la ligne de declaration de la classe.
    }
}
