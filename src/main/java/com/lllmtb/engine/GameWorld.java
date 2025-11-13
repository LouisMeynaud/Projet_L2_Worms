package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameWorld {
    private final Map map;
    private final EventBus bus;
    private final PhysicsEngine physics;
    private final List<Team> teams;
    private final GameLoop gameLoop;

    public GameWorld(GameSettings settings) {
        Map tmap = null;

        // Init Map (we'll have to handle this better once MapLoader throws catchable exceptions)
        System.out.println(settings.mapFileName);
        if (settings.mapFileName != null) {
            try {
                tmap = MapLoader.loadMatrix(settings.mapFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (tmap == null) {
            tmap = GenerateMap.createMap(settings.floatingIsland ? GenerateMap.FLYINGWORLD : GenerateMap.WORLD);
        }
        this.map = tmap;
        //System.out.println(this.map);

        // Init EventBus
        this.bus = new EventBus();

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, settings.hasWind);

        // Init Teams
        this.teams = new ArrayList<>();
        for (int i = 0; i < settings.nbTeams; i++) {
            try {
                Position p = new Position(0, 0);    // dummy position
                Team team = new Team(settings.teamNames[i], settings.nbWorms, p);
                teams.add(team);
                System.out.printf("%d | %s - %d - %s\n", i, team.name, team.id, team.teamColor);
                for (int j = 0; j < settings.nbWorms; j++) {
                    String name = team.name + j;
                    Worm worm = new Worm(name, team, p); // will receive available spawn points from the map
                    registerEntity(worm);
                }
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
        System.out.println("Starting the game loop...");
        this.gameLoop.run();
    }

    public void stop() {
        this.gameLoop.stop();
    }

    public void registerEntity(@NonNull Entity e) {
        physics.add(Objects.requireNonNull(e).getBody());
    }
}
