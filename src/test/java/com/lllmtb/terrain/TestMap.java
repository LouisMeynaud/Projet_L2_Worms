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
            m = MapLoader.createMatrix("mapTest");
    }
    
    @Test
    public void TestisBloc(){
        Position position = new Position(2,2);
        assertFalse(m.isBloc(Imap.BLOC.AIR,position));
        assertTrue(m.isBloc(Imap.BLOC.WATER,position));
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
}
