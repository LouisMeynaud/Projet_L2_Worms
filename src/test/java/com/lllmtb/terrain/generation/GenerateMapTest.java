package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import com.lllmtb.terrain.map.Map;



public class GenerateMapTest {
    @Test
    public void createMapTest() {
        String inputSimule = "mapTest\nN\nfichierAEcraserTest\nO\n";

        InputStream originalIn = System.in;//on garde de coter l'entrer original pour la retablir apres

        System.setIn(new ByteArrayInputStream(inputSimule.getBytes())); // getbytes transforme le string en tableau d' octet pour byteArrayInpustream qui prend un tableau d'octect
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 0, LandScape.HILLY,0);
        Map m =GenerateMap.createMap(configtest);
        assertEquals(60, m.getWidth());//grand valeur pour bien tester les ligne qui retrie les tableau
        assertEquals((int)(60*ConfigMap.RATIOWIDTHHEIGHT), m.getHeight());
        assertEquals("testMap.txt", m.getFilesTxt());
        // On restaure System.in
        System.setIn(originalIn);

    }

    @Test
    public void createMatrixFLYINGISLANDTest() {
        String inputSimule = "nomTest\n";

        InputStream originalIn = System.in;//on garde de coter l'entrer original pour la retablir apres

        System.setIn(new ByteArrayInputStream(inputSimule.getBytes())); // getbytes transforme le string en tableau d' octet pour byteArrayInpustream qui prend un tableau d'octect
        ConfigMap configtest = new ConfigMap(60, 5, 5, false, 0, LandScape.HILLY,0);
        Map m =GenerateMap.createMap(configtest);
        assertEquals(60, m.getWidth());
        assertEquals((int)(60*ConfigMap.RATIOWIDTHHEIGHT), m.getHeight());
        assertEquals("testMap.txt", m.getFilesTxt());
        // On restaure System.in
        System.setIn(originalIn);

    }

    @Test
    void DeclaClassTest(){
        new GenerateMap(); // ligne inutile car class static mais permet de couvrir la ligne de declaration de la classe
    }

    @Test
    void TriHightLineLowLineTest(){
        int[] highLine = {8,2,4};
        int[] lowLine = {7,5,3};
        int[] highLineBis = {8,5,4};
        int[] lowLineBis = {7,2,3};
        ConfigMap config = new ConfigMap(3, 0, 0, false, 0, LandScape.HILLY,0);
        GenerateMap.TriHightLineLowLine(highLine,lowLine,config);
        for (int i = 0; i < 3; i++) {
        assertEquals(highLine[i], highLineBis[i]);
        assertEquals(lowLine[i], lowLineBis[i]);
        }
        
    }
}
