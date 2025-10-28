package com.lllmtb.terrain;

import com.lllmtb.physics.Position;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapTest {
    private Map m;

    @BeforeEach
    public void setUp() {
        m = MapLoader.createMatrix("mapTest");
    }

    @AfterEach
    public void tearDown() {
        m = null;
    }

    @Test
    public void isTileAt_returnsCorrectResultForGivenPositionAndTile() {
        Position position = new Position(2, 2);
        assertFalse(m.isTileAt(position, Tile.AIR));
        assertTrue(m.isTileAt(position, Tile.WATER));
    }

    // Test Map size is 5x6
    @Test
    public void inMap_returnsTrueForPositionInsideMap() {
        int xInside = 3;
        int yInside = 3;
        assertTrue(m.inMap(xInside, yInside));
    }

    @Test
    public void inMap_returnsFalseForPositionOutsideMap() {
        int xOutside = 20;
        int yOutside = 20;
        assertFalse(m.inMap(xOutside, yOutside));
    }

    @Test
    public void inMap_returnsTrueForPositionOnBorder() {
        int xBorder = 4;
        int yBorder = 5;
        assertTrue(m.inMap(xBorder, yBorder));
    }

    @Test
    public void cooMapToCooMatrix_convertsCoordinatesCorrectly() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(4, 5);
        Position position3 = new Position(2, 2);
        assertEquals(0, m.cooMapToCooMatrix(position1).getX());
        assertEquals(5, m.cooMapToCooMatrix(position1).getY());

        assertEquals(4, m.cooMapToCooMatrix(position2).getX());
        assertEquals(0, m.cooMapToCooMatrix(position2).getY());

        assertEquals(2, m.cooMapToCooMatrix(position3).getX());
        assertEquals(3, m.cooMapToCooMatrix(position3).getY());
    }
    @Test
    public void TestsetMap(){
        Position position = new Position(2, 2);
        assertFalse(m.isTILES(Tile.TILES.DIRT , position));
        m.setMap(position, Tile.TILES.DIRT);
        assertTrue(m.isTILES(Tile.TILES.DIRT , position));
        m.setMap(position, Tile.TILES.WATER);

    }
}
