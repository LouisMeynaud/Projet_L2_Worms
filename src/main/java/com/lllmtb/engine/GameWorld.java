package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

public class GameWorld {
    private final Map map;
    private final EventBus bus;
    private final PhysicsEngine physics;
    private final GameLoop gameLoop;

    public GameWorld(GameSettings settings) {

        // Init Map
        if (settings.mapFileName != null) {
            try {
                this.map = MapLoader.loadMatrix(settings.mapFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            this.map = GenerateMap.createMap(settings.floatingIsland ? GenerateMap.FLYINGWORLD : GenerateMap.WORLD);
        }

        // Init EventBus
        this.bus = new EventBus();

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, settings.hasWind);

        // Init GameLoop
        this.gameLoop = new GameLoop(this);
    }

    public Map getMap() {
        return map;
    }

    public EventBus getBus() {
        return bus;
    }

    public PhysicsEngine getPhysics() {
        return physics;
    }

    public void start() {
        this.gameLoop.run();
    }

    public void stop() {
        this.gameLoop.stop();
    }
}
