package com.lllmtb.engine;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.lllmtb.engine.save.GameSave;
import com.lllmtb.engine.save.TeamSave;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.turn.TurnManager;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Move;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.CreateSpawnPoint;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

public class GameWorld {
    private final Map map;
    private final EventBus bus;
    private final PhysicsEngine physics;
    private final List<Team> teams;
    private final GameLoop gameLoop;
    private int indexTeam;
    private final TurnManager turnManager;
    private final long turnDuration;
    private String gameName;
    Thread loopThread;
    public final GameSettings settings;

    private final BlockingQueue<GameCommand> commandQueue;

    public GameWorld(GameSave gameSave, Map map) {
        this.settings = gameSave.settings;
        gameName = settings.mapFileName;
        this.gameName = settings.gameName;
        // Init Map (we'll have to handle this better once MapLoader throws catchable exceptions)

        if (map == null) {
            System.out.println("Generating a new map...");
            ConfigMap config = new ConfigMap(settings.widthMap, 4, 5, settings.floatingIsland, 17, settings.terrainType, settings.nbTeams, settings.nbWorms);
            map = GenerateMap.createMap(config);
        }
        this.map = map;
        if (gameSave.bus == null) {
            this.bus = new EventBus();
        } else {
            this.bus = gameSave.bus;
        }
        Move.setMaxjump(settings.maxJump);

        // Init GameCommand queue
        this.commandQueue = new LinkedBlockingQueue<>();

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, bus, settings.hasWind);

        // Init Teams
        this.teams = new ArrayList<>();
        for (TeamSave saveTeam :gameSave.teams) {
            this.teams.add(new Team(saveTeam));
        }
        // Init GameLoop
        this.gameLoop = new GameLoop(this);
        Logger.logger.debug("Game initialised");

        //Init TurnManager
        this.turnManager = new TurnManager(this, settings.turnDuration);
        this.turnDuration = this.getTurnDuration();
    }

    public GameWorld(GameSettings settings, EventBus eventBus) {
        this.settings = settings;
        gameName = settings.mapFileName;
        Map tmap = null;
        this.gameName = settings.gameName;
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
        this.map.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(settings.nbTeams, settings.nbWorms, tmap));

        this.bus = eventBus;
        Move.setMaxjump(settings.maxJump);

        // Init GameCommand queue
        this.commandQueue = new LinkedBlockingQueue<>();

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, bus, settings.hasWind);

        // Init Teams
        this.teams = new ArrayList<>();
        Logger.logger.info("Creating the Teams...");
        for (int i = 0; i < settings.nbTeams; i++) {
            try {
                Worm.setMaxHp(settings.maxHp);
                Team team = new Team(settings.teamNames[i], settings.nbWorms, tmap.getSpawnPoint()[i]);
                teams.add(team);
                team.getTeamWorms().forEach(this::registerEntity);
                Logger.logger.info("{} | {} - {} - {}\n", i, team.name, team.id, team.teamColor);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        // Init GameLoop
        this.gameLoop = new GameLoop(this);
        Logger.logger.debug("Game initialised");

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

    public Team getPlayingTeam() {
        return teams.get(indexTeam);
    }

    public TurnManager getTurnManager() {
        return this.turnManager;
    }

    public long getTurnDuration() {
        return this.turnManager.getTurnDuration();
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public int getIndexTeam() {
        return indexTeam;
    }

    public String getGameName() {
        return gameName;
    }

    public void start() {
        if (loopThread != null && loopThread.isAlive()) return;
        Logger.logger.debug("Starting the game loop...");
        loopThread = new Thread(gameLoop, "gameloop");
        this.startTeamTurn();
        loopThread.start();
    }

    public void pause(boolean paused) {
        if (loopThread == null || !loopThread.isAlive()) return;
        Logger.logger.info("{} the game loop...", paused ? "Pausing" : "Resuming");
        gameLoop.pause(paused);
        bus.setLocked(paused);
    }

    public void stop() {
        Logger.logger.debug("Stopping the game loop...");
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

    public void update(double tickTime) {
        processCommands();
        physics.next(tickTime);
        turnManager.updateTurn();
        bus.postQueued(new GameStateUpdatedEvent());
    }


    public void startTeamTurn() {
        if (!teams.isEmpty()) {
            this.turnManager.startTurn(teams.get(indexTeam));
        }
    }

    public void nextTeamTurn() {
        if (!teams.isEmpty()) {
            this.turnManager.startTurn(teams.get(indexTeam++));
            if (indexTeam >= teams.size()) {
                indexTeam = 0;
            }
        }
    }

    /**
     * Queue a GameCommand to be executed in the next update cycle.
     * @param cmd the GameCommand to queue
     * @return true if the command was successfully queued, false otherwise
     */
    public boolean queueCommand(GameCommand cmd) {
        if (cmd == null) {
            return false;
        }
        return commandQueue.offer(cmd);
    }

    /**
     * Queue multiple GameCommands to be executed in the next update cycle.
     * @param cmds list of GameCommands to queue
     * @return true if all commands were successfully queued, false otherwise
     */
    public boolean queueCommands(List<GameCommand> cmds) {
        boolean allQueued = true;
        for (GameCommand cmd : cmds) {
            if (!queueCommand(cmd)) allQueued = false;
        }
        return allQueued;
    }

    /**
     * Process and execute all queued GameCommands in order.
     * This method is called during the update cycle.
     * If a command throws an exception, it is caught and logged, allowing subsequent commands to be processed.
     */
    public void processCommands() {
        List<GameCommand> drained = new ArrayList<>();
        commandQueue.drainTo(drained);
        for (GameCommand cmd : drained) {
            try {
                cmd.execute(this);
            } catch (Exception ex) {
                Logger.logger.error("Error commands : {} ", ex.getMessage(), ex);
            }
        }
    }
}
