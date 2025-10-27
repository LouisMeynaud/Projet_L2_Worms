package com.lllmtb.terrain;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertPathValidatorException.BasicReason;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMap {
    static Map m;

    @BeforeAll
    public static void setup() {
            m = MapLoader.createMatrix2("mapTest");
    }
    
    @Test
    public void TestisTILES(){
        Position position = new Position(2,2);
        assertFalse(m.isTILES(Tile.TILES.AIR,position));
        assertTrue(m.isTILES(Tile.TILES.WATER,position));
    }

    @Test
    public void TestinMap(){
        int xdedans =3;
        int ydedans =3;
        int xbordure=4;
        int ybordure=5;
        int xdehors = 20;
        int ydehors = 20;
        assertTrue(m.inMap(xdedans,ydedans));
        assertFalse(m.inMap(xdehors,ydehors));
        assertTrue(m.inMap(xbordure,ybordure));
    }

    @Test
    public void TestcooMapToCooMatrix(){
        Position position1 = new Position(0,0);
        Position position2 = new Position(4,5);
        Position position3 = new Position(2,2);
        assertEquals(m.cooMapToCooMatrix(position1).getPosx(),0 );
        assertEquals(m.cooMapToCooMatrix(position1).getPosy(),5 );

        assertEquals(m.cooMapToCooMatrix(position2).getPosx(),4 );
        assertEquals(m.cooMapToCooMatrix(position2).getPosy(),0 );

        assertEquals(m.cooMapToCooMatrix(position3).getPosx(),2 );
        assertEquals(m.cooMapToCooMatrix(position3).getPosy(),3 );
    }
}
