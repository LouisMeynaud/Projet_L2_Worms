package com.lllmtb.engine;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.turn.TurnManager;
import com.lllmtb.engine.save.GameSave;
import com.lllmtb.engine.save.PhysicsBodySave;
import com.lllmtb.engine.save.TeamSave;
import com.lllmtb.engine.turn.Turn;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.shop.ShopManager;
import com.lllmtb.physics.GameMode;
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
    private final String gameName;
    private final GameMode gameMode;
    private ConfigMap configMap;
    private final ShopManager shopManager;
    private final BlockingQueue<GameCommand> commandQueue;
    private final List<Team> teams;
    private final TurnManager turnManager;
    private int indexTeam;
    private final GameLoop gameLoop;
    protected Thread loopThread;

    public GameWorld(GameSave gameSave, Map map) {
        Logger.logger.info("Loading GameWorld from save...");
        GameSettings settings = gameSave.gameSettings;
        this.gameName = gameSave.gameSettings.gameName;
        this.indexTeam = gameSave.indexTeam;
        // Init Map - fail if map is null
        if (map == null) {
            Logger.logger.error("Cannot load game: Map is null");
            throw new IllegalArgumentException("Map cannot be null when loading a saved game");
        }

        this.map = map;
        this.bus = Objects.requireNonNullElseGet(gameSave.bus, EventBus::new);
        Move.setMaxjump(settings.maxJump);

        // Init GameCommand queue
        this.commandQueue = new LinkedBlockingQueue<>();

        //Init gameMode
        this.gameMode = settings.gameMode;


        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, this, settings.hasWind, settings.gameMode);
        for (PhysicsBodySave physicsBodySave : gameSave.entities) {
            physics.add(physicsBodySave.physicsBody);
        }

        // Init Teams
        this.teams = new ArrayList<>();
        for (TeamSave saveTeam :gameSave.teams) {
            this.teams.add(new Team(saveTeam));
        }
        // Init GameLoop
        this.gameLoop = new GameLoop(this);
        Logger.logger.debug("Game initialised");

        //Init TurnManager
        this.turnManager = gameSave.turnManager;

        try {
            Field worldField = TurnManager.class.getDeclaredField("world");
            worldField.setAccessible(true);
            worldField.set(this.turnManager, this);
            Field turnField = TurnManager.class.getDeclaredField("turn");
            turnField.setAccessible(true);
            turnField.set(this.turnManager, new Turn(this.getPlayingTeam()));
            Field restartField = TurnManager.class.getDeclaredField("restart");
            restartField.setAccessible(true);
            restartField.set(this.turnManager, true);
            Field nbTurnField = TurnManager.class.getDeclaredField("nbTurn");
            nbTurnField.setAccessible(true);
            nbTurnField.set(this.turnManager, this.turnManager.getNbTurn() - 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.turnManager.init();

        //Init FriendlyFire
        Projectile.setFriendlyFire(settings.friendlyFire);

        //Init ShopManager
        this.shopManager = new ShopManager(this.bus);

        //Init credit from current Team
        this.teams.get(indexTeam).removeCredits(150);
        Logger.logger.info("Game successfully loaded from save");
    }

    public GameWorld(GameSettings settings, EventBus eventBus) {
        Map tmap = null;
        this.gameName = settings.gameName;
        // Try to load map from file
        if (settings.mapFileName != null) {
            try {
                tmap = MapLoader.loadMap(settings.mapFileName);
                this.configMap = new ConfigMap(tmap.getWidth(), tmap.getHeightWater(),0, false,0, settings.terrainType, settings.gameMode);
            } catch (Exception e) {
                Logger.logger.error("Failed to load map '{}'", settings.mapFileName, e);
                throw new RuntimeException(e);
            }
        }
        if (tmap == null) {
            Logger.logger.info("Generating a new map...");
            this.configMap = settings.configMap;
            this.configMap.setGameMode(settings.gameMode);
            tmap = GenerateMap.createMap(this.configMap);
        }
        this.map = tmap;
        this.map.setSpawnPoint(CreateSpawnPoint.createSpawnPoint(settings.nbTeams, settings.nbWorms, tmap));
        this.bus = eventBus;
        Move.setMaxjump(settings.maxJump);

        // Init GameCommand queue
        this.commandQueue = new LinkedBlockingQueue<>();

        //Init gameMode
        this.gameMode = settings.gameMode;

        // Init PhysicsEngine
        this.physics = new PhysicsEngine(map, this, settings.hasWind, gameMode);

        // Init Teams
        this.teams = new ArrayList<>();
        Logger.logger.info("Creating the Teams...");
        for (int i = 0; i < settings.nbTeams; i++) {
            try {
                Worm.setMaxHp(settings.maxHp);
                int coin = settings.coinCheat ? -1 : 0;
                Team team = new Team(settings.teamNames[i], settings.nbWorms, tmap.getSpawnPoint()[i], coin);
                teams.add(team);
                team.getTeamWorms().forEach(this::registerEntity);
                Logger.logger.info("{} | {} - {} - {}\n", i, team.name, team.id, team.teamColor);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        //Init FriendlyFire
        Projectile.setFriendlyFire(settings.friendlyFire);

        // Init GameLoop
        this.gameLoop = new GameLoop(this);

        //Init TurnManager
        this.turnManager = new TurnManager(this, settings.turnDuration);

        //Init ShopManager
        this.shopManager = new ShopManager(this.bus);
        Logger.logger.info("Game successfully initialised");
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
        if (teams.isEmpty()) return null;
        if (indexTeam < 0) return teams.get(0);
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

    public GameMode getGameMode() {
        return gameMode;
    }

    public ShopManager getShopManager() {
        return this.shopManager;
    }

    public ConfigMap getConfigMap() {
        return this.configMap;
    }

    public void start() {
        if (loopThread != null && loopThread.isAlive()) return;
        Logger.logger.debug("Starting the game loop...");
        loopThread = new Thread(gameLoop, "gameloop");
        loopThread.start();
        this.indexTeam--;
        nextTeamTurn();
    }

    public void pause(boolean paused) {
        if (loopThread == null || !loopThread.isAlive()) return;
        Logger.logger.info("{} the game loop...", paused ? "Pausing" : "Resuming");
        gameLoop.pause(paused);
        bus.setLocked(paused);
    }

    public void stop() {
        Logger.logger.debug("Stopping the game loop...");
        turnManager.cleanup();
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
        turnManager.updateTurn(tickTime);
        physics.update(tickTime);
        bus.postQueued(new GameStateUpdatedEvent());
    }

    public void nextTeamTurn() {
        if (teams.isEmpty()) {
            Logger.logger.warn("No teams available to take a turn.");
            return;
        }
        do {
            indexTeam = (indexTeam + 1) % teams.size();
        } while (!teams.get(indexTeam).oneAlive());
        this.turnManager.startTurn(teams.get(indexTeam));
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
        if (cmds == null || cmds.isEmpty()) return false;
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
