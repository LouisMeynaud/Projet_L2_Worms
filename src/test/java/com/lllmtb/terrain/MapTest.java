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
    public void inMap_returnsfalseForXinYOut() {
        int xIn = 3;
        int yOut = 6;
        assertFalse(m.inMap(xIn, yOut));
    }

    @Test
    public void inMap_returnsfalseForXOutYIn() {
        int xOut = 3;
        int yIn= 6;
        assertFalse(m.inMap(xOut, yIn));
    }

    @Test
    public void inMap_returnsfalseForPositionNegatif() {
        int xOut = -1;
        int yIn= -1;
        assertFalse(m.inMap(xOut, yIn));
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
    public void setMap_editsMapCorrectly() {
        Position position = new Position(2, 2);
        assertFalse(m.isTileAt(position, Tile.DIRT));
        m.setTile(position, Tile.DIRT);
        assertTrue(m.isTileAt(position, Tile.DIRT));
    }

    @Test
    public void toStringTest() {
        String attentue = "width : 5, height : 6, matrice : \n" +
                "\n" +
                "     \n" +
                "     \n" +
                "#####\n" +
                "#oo##\n" +
                "ooooo\n" +
                "ooooo";
            System.out.println(attentue);
            System.out.println(m);
            assertEquals(attentue, m.toString());
    }
}
