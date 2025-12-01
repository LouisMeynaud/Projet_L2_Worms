package com.lllmtb.terrain.map;

import com.lllmtb.physics.Position;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapTest {
    private Map m;

    @BeforeEach
    public void setUp() {
        m = MapLoader.loadMatrix("mapTest");
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
        Position position = new Position(3, 3);
        assertTrue(m.inMap(position)); // toutes conditions true
    }

    @Test
    public void inMap_xFalse() {
        Position position = new Position(-1, 3);
        assertFalse(m.inMap(position)); // 0<=x false
    }

    @Test
    public void inMap_yFalse() {
        Position position = new Position(3, -1);
        assertFalse(m.inMap(position)); // 0<=y false
    }

    @Test
    public void inMap_xBorderFalse() {
        Position position = new Position(5, 3);
        assertFalse(m.inMap(position)); // x < width false
    }

    @Test
    public void inMap_yBorderFalse() {
        Position position = new Position(3, 6);
        assertFalse(m.inMap(position)); // y < height false
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
                "   ~ \n" +
                "###_#\n" +
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
    @Test
    void isSolidTest() {
        Position positionSolide = new Position(0, 3);
        Position positionNoSolide = new Position(0, 4);
        assertTrue(m.isSolid(positionSolide));
        assertFalse(m.isSolid(positionNoSolide));
    }

    @Test
    void testSetFiles(){
        m.setFilesTxt("test");
        assertEquals(m.getFilesTxt(), "test");
    }

}
