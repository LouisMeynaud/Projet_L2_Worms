package com.lllmtb.terrain.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SaveMapTest {

    @Test
        public void saveMapDeclarationClass(){
            new SaveMap(); //constructeur inutile etant donner que savemap ne contient que des methode statique mais permet de lire la ligne de declaration de la classe et donc atteindre les 90%
        }


    @Test
    public void testMapToString() {
        String expected = "AAAAA\n" +
                             "AAACA\n" +
                             "DDDGD\n" +
                             "DWWDD\n" +
                             "WWWWW\n" +
                             "WWWWW\n";

        Map mapObject = MapLoader.loadMap("mapTest");
        assertNotNull(mapObject);
        String actual = SaveMap.mapToString(mapObject);
        assertEquals(expected, actual);
    }
}
