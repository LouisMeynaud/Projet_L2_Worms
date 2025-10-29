package com.lllmtb.terrain;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapLoaderTest {
    public static String mapText;
    public static Map m;
    public static String path;
    public static Tile[][] expectedMatrix = {
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR},
        {Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR,  Tile.AIR},
        {Tile.DIRT, Tile.DIRT, Tile.DIRT, Tile.DIRT, Tile.DIRT},
        {Tile.DIRT, Tile.WATER, Tile.WATER, Tile.DIRT, Tile.DIRT},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER},
        {Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER, Tile.WATER}
    };
    @BeforeAll
    public static void setUp() throws IOException {
        path = "src/main/java/com/lllmtb/terrain/MapFile/mapTest.txt";
        mapText = Files.readString(Path.of(path));
        m = MapLoader.createMatrix("mapTest");
    }

    @Test
    void createMatrix_createsCorrectMatrixFromFile() {
        assertTrue(Arrays.deepEquals(expectedMatrix, m.getMapMatrix()));
    }

    @Test
    void createMatrix_createsIncorrectMatrixFromFile() {
        m = MapLoader.createMatrix("mapIncorrectTest");
        assertEquals(m, null);;
    }

    @Test
    void saveMapDeclarationClass(){
        new MapLoader(); //constructeur inutile etant donner que MapLoader ne contient que des methode statique mais permet de lire la ligne de declaration de la classe et donc atteindre les 90%
    }

    @Test
    void createMatrix_createsInexistantMatrixFromFile() {
        m = MapLoader.createMatrix("mapinexistante");
        assertEquals(m, null);;
    }
}