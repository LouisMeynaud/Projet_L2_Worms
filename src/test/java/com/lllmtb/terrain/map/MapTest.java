package com.lllmtb.terrain.map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;

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
        assertEquals(attentue, m.toString());
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

    @Test
    public void getTile_outOfBounds_yTooLarge() {
        Position position = new Position(2, 6); // y >= height -> coomapToMatrix < 0
        assertEquals(Tile.AIR, m.getTile(position));
    }

    @Test
    public void getTile_outOfBounds_yNegative() {
        Position position = new Position(2, -1); // y < 0 -> posMatrix.y >= mapMatrix.length
        assertEquals(Tile.AIR, m.getTile(position));
    }

    @Test
    public void getTile_outOfBounds_xNegative() {
        Position position = new Position(-1, 2); // x < 0
        assertEquals(Tile.AIR, m.getTile(position));
    }

    @Test
    public void getTile_outOfBounds_xTooLarger() {
        Position position = new Position(5, 2); // x >= width
        assertEquals(Tile.AIR, m.getTile(position));
    }

    @Test
    public void getTile_inBounds_returnsCorrectTile() {
        Position position = new Position(2, 2);
        assertEquals(Tile.WATER, m.getTile(position));
    }

    @Test
    void testSetAndGetNbTeam() {
        ConfigMap config = new ConfigMap(100, 10, 10, false, 10, LandScape.FLAT, 0, 0, null);
        config.setNbTeam(5);
        assertEquals(5, config.getNbTeam());
    }

    @Test
    void testSetAndGetNbWarms() {
        ConfigMap config = new ConfigMap(100, 10, 10, false, 10, LandScape.FLAT, 0, 0, null);
        config.setNbWarms(8);
        assertEquals(8, config.getNbWarms());
    }

    @Test
    public void testRisingWater() {
        int width = 4;
        int height = 5;
        Map map = new Map(height, width, "test.txt");

        for (int x = 0; x < width; x++) {
            map.setTile(new Position(x, 0), Tile.WATER);
            map.setTile(new Position(x, 1), Tile.WATER);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 2; y < height; y++) {
                map.setTile(new Position(x, y), Tile.AIR);
            }
        }

        map.setHeightWater(2);
        assertEquals(2, map.getHeightWater());
        boolean result = map.risingWater();
        assertTrue(result);
        assertEquals(3, map.getHeightWater());
        assertEquals(Tile.WATER, map.getTile(new Position(3, 2)));
        assertEquals(Tile.AIR, map.getTile(new Position(2, 3)));
    }

    @Test
    public void testInitializeHeightWater() {
        int width = 4;
        int height = 5;
        Map map = new Map(height, width, "test.txt");

        for (int x = 0; x < width; x++) {
            map.setTile(new Position(x, 0), Tile.WATER);
            map.setTile(new Position(x, 1), Tile.WATER);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 2; y < height; y++) {
                map.setTile(new Position(x, y), Tile.AIR);
            }
        }

        map.initializeHeightWater();
        assertEquals(2, map.getHeightWater());
    }

    @Test
    public void testRisingWaterWithHeightToHeight() {
        int width = 4;
        int height = 5;
        Map map = new Map(height, width, "test.txt");

        for (int x = 0; x < width; x++) {
            map.setTile(new Position(x, 0), Tile.WATER);
            map.setTile(new Position(x, 1), Tile.WATER);
            map.setTile(new Position(x, 2), Tile.WATER);
            map.setTile(new Position(x, 3), Tile.WATER);
            map.setTile(new Position(x, 4), Tile.WATER);
        }

        map.setHeightWater(5);
        assertEquals(5, map.getHeightWater());
        boolean result = map.risingWater();
        assertFalse(result);
    }
}
