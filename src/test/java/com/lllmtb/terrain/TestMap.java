package com.lllmtb.terrain;

import com.lllmtb.physics.Position;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMap {
    static Map m;

    @BeforeAll
    public static void setup() {
        m = MapLoader.createMatrix("mapTest");
    }

    @Test
    public void TestisTILES() {
        Position position = new Position(2, 2);
        assertFalse(m.isTILES(Tile.TILES.AIR, position));
        assertTrue(m.isTILES(Tile.TILES.WATER, position));
    }

    @Test
    public void TestinMap() {
        int xdedans = 3;
        int ydedans = 3;
        int xbordure = 4;
        int ybordure = 5;
        int xdehors = 20;
        int ydehors = 20;
        assertTrue(m.inMap(xdedans, ydedans));
        assertFalse(m.inMap(xdehors, ydehors));
        assertTrue(m.inMap(xbordure, ybordure));
    }

    @Test
    public void TestcooMapToCooMatrix() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(4, 5);
        Position position3 = new Position(2, 2);
        assertEquals(m.cooMapToCooMatrix(position1).getX(), 0);
        assertEquals(m.cooMapToCooMatrix(position1).getY(), 5);

        assertEquals(m.cooMapToCooMatrix(position2).getX(), 4);
        assertEquals(m.cooMapToCooMatrix(position2).getY(), 0);

        assertEquals(m.cooMapToCooMatrix(position3).getX(), 2);
        assertEquals(m.cooMapToCooMatrix(position3).getY(), 3);
    }
}
