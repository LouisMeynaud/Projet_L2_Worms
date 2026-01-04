package com.lllmtb.engine.save;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;


import java.io.FileNotFoundException;

import static com.lllmtb.engine.save.Save.gameWorld;

public class LoadTest{
    public static void main(String[] args) throws FileNotFoundException {
        GameSave gameSave = Load.loadGameSave("e_save");
        Load.reforgeGameSave(gameSave);
        Map map = MapLoader.loadMap("e_save");

        for (PhysicsBodySave physicsBodySave : gameSave.entities){
            System.out.println(physicsBodySave.entity);
        }
        GameWorld gameWorld = new GameWorld(gameSave, map);
        System.out.println(gameSave.entities.toString());
        for (PhysicsBody physicsBody: gameWorld.getPhysics().getBodies()){
            System.out.println(physicsBody.getEntity().toString());
        }
        System.out.println(gameWorld.getTeams().get(0).getInventory().toString());
    }
}
