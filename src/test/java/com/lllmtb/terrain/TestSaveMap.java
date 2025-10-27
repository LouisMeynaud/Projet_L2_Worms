package com.lllmtb.terrain;
import com.lllmtb.physics.Position;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestSaveMap {
    public static Map m;
    Position position = new Position(0, 0);
    public static Tile.TILES[][] map_attendu = {
            { Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR },
            { Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR, Tile.TILES.AIR },
            { Tile.TILES.DIRT, Tile.TILES.DIRT, Tile.TILES.DIRT, Tile.TILES.DIRT, Tile.TILES.DIRT },
            { Tile.TILES.DIRT, Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.DIRT, Tile.TILES.DIRT },
            { Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.WATER },
            { Tile.TILES.AIR, Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.WATER, Tile.TILES.WATER }
    };

    @BeforeAll
    public static void setup() {
        m = MapLoader.createMatrix("mapTest");
    }

    @Test
    //test sauvegarde map existante
    public void TestSave() {
        m.setMap(position, Tile.TILES.AIR); // on modif une case
        SaveMap.saveMap(m);// on sauvegarde
        m = MapLoader.createMatrix("mapTest");// on la recree a partir du fichier sauvegarder pour voir si la modif a bien ete sauvegarder
        for(int i=0;i<map_attendu.length;i++){
            for(int j=0;j<map_attendu[0].length;j++){
        
        assertEquals(m.getMapMatrix()[i][j],map_attendu[i][j]);
            }
    }
    }
    @AfterEach
    public void restoreMap(){
        m.setMap(position, Tile.TILES.WATER);
        SaveMap.saveMap(m);
    }
}
