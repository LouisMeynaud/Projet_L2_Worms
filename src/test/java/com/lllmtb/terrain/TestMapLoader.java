package com.lllmtb.terrain;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class TestMapLoader {
    public static String mapText;
    public static Map m;
    public static String path;
    public static Imap.BLOC[][] map_attendu = {
        {Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR},
        {Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR,  Imap.BLOC.AIR},
        {Imap.BLOC.DIRT, Imap.BLOC.DIRT, Imap.BLOC.DIRT, Imap.BLOC.DIRT, Imap.BLOC.DIRT},
        {Imap.BLOC.DIRT, Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.DIRT, Imap.BLOC.DIRT},
        {Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER},
        {Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER, Imap.BLOC.WATER}
    };
    @BeforeAll
    public static void setup() {
        try {
            path = "src/main/java/com/lllmtb/terrain/MapFile/mapTest.txt";
            mapText = Files.readString(Path.of(path));
            m = MapLoader.createMatrix("mapTest");
            

        } catch (
        IOException e) {
            System.out.println(e);
        }

    }

    @Test
    void TestcreateMatrix(){
        for(int i=0;i<map_attendu.length;i++){
            for(int j=0;j<map_attendu[0].length;j++){
        
        assertEquals(m.getMapMatrix()[i][j],map_attendu[i][j]);
            }
    }
}
}