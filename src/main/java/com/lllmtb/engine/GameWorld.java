package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.CreateSpawnPoint;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.lllmtb.events.TheEvents.GameStateUpdatedEvent;
import com.lllmtb.terrain.generation.ConfigMap;

public class GameWorld {
    private final Map map;
    private final EventBus bus;
    private final PhysicsEngine physics;
    private final List<Team> teams;
    private final GameLoop gameLoop;
    Thread loopThread;

    public GameWorld(GameSettings settings,EventBus eventBus) {
        Map tmap = null;

        // Init Map (we'll have to handle this better once MapLoader throws catchable exceptions)
        if (settings.mapFileName != null) {
            try {
                tmap = MapLoader.loadMatrix(settings.mapFileName);
                tmap.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(settings.nbTeams,settings.nbWorms,tmap));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (tmap == null) {
            System.out.println("Generating a new map...");
            ConfigMap config = new ConfigMap(settings.widthMap, 4, 5, settings.floatingIsland, 17, settings.terrainType,settings.nbTeams,settings.nbWorms);
            tmap = GenerateMap.createMap(config);
            tmap.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(settings.nbTeams,settings.nbWorms,tmap));
        }
        this.map = tmap;
        System.out.println(this.map);

        // Init EventBus
        this.bus = eventBus;

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, settings.hasWind);

        // Init Teams
        this.teams = new ArrayList<>();
        System.out.println("Creating the Teams...");
        for (int i = 0; i < settings.nbTeams; i++) {
            try {
                Team team = new Team(settings.teamNames[i], settings.nbWorms, tmap.getSpawnPoint()[i]);
                teams.add(team);
                team.getTeamWorms().forEach(this::registerEntity);
                System.out.printf("%d | %s - %d - %s\n", i, team.name, team.id, team.teamColor);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        // Init GameLoop
        this.gameLoop = new GameLoop(this);
        System.out.println("Game initialised");
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

    public List<Team> getTeams() {
        return teams;
    }

    public void start() {
        if (loopThread != null && loopThread.isAlive()) return;
        System.out.println("Starting the game loop...");
        loopThread = new Thread(gameLoop, "gameloop");
        loopThread.start();
    }

    public void stop() {
        System.out.println("Stopping the game loop...");
        gameLoop.stop();
        if (loopThread != null) {
            loopThread.interrupt();
            try {
                loopThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void registerEntity(@NonNull Entity e) {
        physics.add(Objects.requireNonNull(e).getBody());
    }

    public void update(double tickTime){
        this.getPhysics().next(tickTime);
        bus.postQueued(new GameStateUpdatedEvent());
        
    }
}
