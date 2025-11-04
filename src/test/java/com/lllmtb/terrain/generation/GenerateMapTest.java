package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import com.lllmtb.terrain.map.Map;

public class GenerateMapTest {
    @Test
    public void createMatrixTest() {
        String inputSimule = "mapTest\nN\nmapTest\nN\nnomTest\n";

        InputStream originalIn = System.in;//on garde de coter l'entrer original pour la retablir apres

        System.setIn(new ByteArrayInputStream(inputSimule.getBytes())); // getbytes transforme le string en tableau d' octet pour byteArrayInpustream qui prend un tableau d'octect
        ConfigMap configtest = new ConfigMap(60, 100, 2, 5, 5, 0, false, 20);
        Map m =GenerateMap.createMatrix(configtest);
        assertEquals(60, m.getWidth());
        assertEquals(100, m.getHeight());
        assertEquals("nomTest.txt", m.getFilesTxt());
        // On restaure System.in
        System.setIn(originalIn);

    }

    @Test
    void DeclaClassTest(){
        new GenerateMap(); // ligne inutile car class static mais permet de couvrir la ligne de declaration de la classe
    }
}
