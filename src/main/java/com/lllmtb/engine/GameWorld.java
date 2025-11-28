package com.lllmtb.engine;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.events.Event;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.map.CreateSpawnPoint;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameWorld {
    private final Map map;
    private final EventBus bus;
    private final PhysicsEngine physics;
    private final List<Team> teams;
    private final GameLoop gameLoop;
    private int indexTeam;
    private final TurnManager turnManager;
    private final long turnDuration;
    Thread loopThread;

    private final BlockingQueue<GameCommand> commandQueue;


    public GameWorld(GameSettings settings, EventBus eventBus) {
        Map tmap = null;

        // Init Map (we'll have to handle this better once MapLoader throws catchable exceptions)
        if (settings.mapFileName != null) {
            try {
                tmap = MapLoader.loadMatrix(settings.mapFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (tmap == null) {
            System.out.println("Generating a new map...");
            ConfigMap config = new ConfigMap(settings.widthMap, 4, 5, settings.floatingIsland, 17, settings.terrainType, settings.nbTeams, settings.nbWorms);
            tmap = GenerateMap.createMap(config);
        }
        this.map = tmap;
        this.map.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(settings.nbTeams,settings.nbWorms,tmap));

        this.bus = eventBus;

        // Init GameCommand queue
        this.commandQueue = new LinkedBlockingQueue<>();

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, bus, settings.hasWind);

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

        //Init TurnManager
        this.turnManager = new TurnManager(this, settings.turnDuration);
        this.turnDuration = this.getTurnDuration();
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

    public int getIndexTeam(){
        return indexTeam;
    }

    public TurnManager getTurnManager(){
        return this.turnManager;
    }

    public long getTurnDuration(){
        return this.turnManager.getTurnDuration();
    }

    public void start() {
        if (loopThread != null && loopThread.isAlive()) return;
        System.out.println("Starting the game loop...");
        loopThread = new Thread(gameLoop, "gameloop");
        this.nextTeamTurn();
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
        processCommands();
        physics.next(tickTime);
        turnManager.updateTurn();
        bus.postQueued(new GameStateUpdatedEvent());
    }


    public void nextTeamTurn() {
        if(!teams.isEmpty()) {
            this.turnManager.startTurn(teams.get(indexTeam++));
            if (indexTeam >= teams.size()) indexTeam = 0;
        }
    }

    public boolean queueCommand(GameCommand cmd) {
        if (cmd == null) return false;
        return commandQueue.offer(cmd);
    }

    // Minimal function to process GameCommands
    public void processCommands() {
        List<GameCommand> drained = new ArrayList<>();
        commandQueue.drainTo(drained);
        for (GameCommand cmd : drained) cmd.execute(this);
    }
}
