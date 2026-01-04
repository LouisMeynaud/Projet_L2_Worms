package com.lllmtb.terrain.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.map.CreateSpawnPoint;
import com.lllmtb.terrain.map.Map;

public class CreateSpawnPointTest {
    
    @Test
    public void goodspawnPointtest() {
        
        int nbVers = 6;
        int nbTeams= 1;
        int waterHeight = 2;
        ConfigMap configtest = new ConfigMap(100, waterHeight, 5, false, 0, LandScape.HILLY,nbTeams,nbVers,GameMode.NORMAL);
        Map m =GenerateMap.createMap(configtest);
        m.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(nbTeams,nbVers,m));
        assertEquals(nbTeams, m.getSpawnPoint().length);
        for(int j = 0;j < nbTeams;j++){
        for (int i = 0 ; i < nbVers ; i++) {
            assertTrue(m.getSpawnPoint()[j][i].getY()>waterHeight);
        }}
        

    }
}
