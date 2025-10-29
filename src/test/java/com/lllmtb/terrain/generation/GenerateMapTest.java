package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.Map;

public class GenerateMapTest {
    @Test
    public void testcreateMatrix() {
        String inputSimule = "mapTest\nN\nmapTest\nN\nnomTest\n";

        InputStream originalIn = System.in;

        System.setIn(new ByteArrayInputStream(inputSimule.getBytes())); // getbytes transforme le string en tableau d' octet pour byteArrayInpustream qui prend un tableau d'octect

        Map m =GenerateMap.createMatrix(20, 10, 2, 5, 5);
        assertEquals(20, m.getWidth());
        assertEquals(10, m.getHeight());
        assertEquals("nomTest.txt", m.getFilesTxt());
        // On restaure System.in
        System.setIn(originalIn);

    }
}
