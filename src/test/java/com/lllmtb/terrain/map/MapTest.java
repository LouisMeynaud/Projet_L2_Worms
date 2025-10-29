package com.lllmtb.terrain.map;

import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import com.lllmtb.terrain.map.Tile;

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
    public void inMap_allTrue() {
        assertTrue(m.inMap(3, 3)); // toutes conditions true
    }

    @Test
    public void inMap_xFalse() {
        assertFalse(m.inMap(-1, 3)); // 0<=x false
    }

    @Test
    public void inMap_yFalse() {
        assertFalse(m.inMap(3, -1)); // 0<=y false
    }

    @Test
    public void inMap_xBorderFalse() {
        assertFalse(m.inMap(5, 3)); // x < width false
    }

    @Test
    public void inMap_yBorderFalse() {
        assertFalse(m.inMap(3, 6)); // y < height false
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

    @Test
    void testInvalideElementMapToString() {
        Map fakeMap = new Map(1, 1, "test");

        Position position = new Position(0, 0);
        fakeMap.setTile(position, Tile.UNKNOWN);

        try {
            fakeMap.toString();
            fail("IllegalStateException attendue mais non levée");
        } catch (IllegalStateException e) {
        }
    }
}
