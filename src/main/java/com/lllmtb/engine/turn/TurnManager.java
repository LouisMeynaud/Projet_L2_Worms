package com.lllmtb.engine.turn;

import java.util.List;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.save.Save;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.game.*;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.events.input.UseInputEvent;
import com.lllmtb.events.input.MoveInputEvent;
import com.lllmtb.events.turn.ChangeWormsEvent;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnFinishEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Move;
import static com.lllmtb.physics.Move.move;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

/**
 * Controls the turn system
 * <p>
 * This manager handles the full lifecycle of team turns, including:
 * <ul>
 *   <li>Turn start and end logic</li>
 *   <li>Turn timer countdown</li>
 *   <li>Team passive income and coin spawning</li>
 *   <li>Checking turn clean before switching turns</li>
 * </ul>
 * <p>
 * A new turn only begins when the world is in a stable state (no active projectiles, all worms on ground).
 * <p>
 *
 */
public final class TurnManager {

    // Champs
    private Turn turn;
    private final GameWorld world;
    private final long turnDurationInMs;
    private long remainingTimeInMs;
    private int nbTurn;
    private boolean turnFinish = false;
    private static int remainingTimeAfterShootInMs = 5000;

    /**
     * Create a new {@code TurnManager} for the current gameWorld
     * @param world the current {@link GameWorld}
     * @param turnDuration the maximum turn duration for a turn
     */
    public TurnManager(GameWorld world, long turnDuration) {
        this.world = world;
        this.turnDurationInMs = 1000 * turnDuration;
        this.init();
    }

    /**
     * Return the current gameWorld
     */
    public GameWorld getWorld() {
        return this.world;
    }
    /**
     * Return the current maximum turn duration
     */
    public long getTurnDuration() {
        return this.turnDurationInMs;
    }

    /**
     * Return the remainingTime for the current turn.
     */
    public long getRemainingTime() {
        return this.remainingTimeInMs;
    }


    /**
     * Returns the active {@link Turn} object containing current team and worm data.
     * @return the current {@link Turn}
     */
    public Turn getTurn() {
        return turn;
    }

    /**
     * Returns the number of turns that have started since the beginning of the match.
     * @return total turn count
     */
    public int getNbTurn() {
        return this.nbTurn;
    }

    /**
     * Initialize the turn manager with all event listeners.
     */
    public void init() {
        setupEvents();
    }

    /**
     * Set RemainingTimeAfterShoot to a value specified
     * @param value the value specified
     */
    public static void setRemainingTimeAfterShootInMs(int value) {
        TurnManager.remainingTimeAfterShootInMs = value;
    }

    /**
     * Start a new turn for the selected {@link Team}
     * This method reset timer, give credits to the Team, spawnCoins on the map.
     * @param team the selected team who will play.
     */
    public void startTurn(Team team) {
        this.remainingTimeInMs = turnDurationInMs;
        this.turn = new Turn(team);
        this.nbTurn += 1;

        //economy passive income at the start of the turn
        int passiveIncome = 150; //ajout de beaucoup d'argent pour pouvoir tester les arme et le shop
        team.addCredits(passiveIncome);
        Logger.logger.info("l'equipe : " + team.name + "gagne :" + passiveIncome + "pour le debut du tour");
        spawnCoins();
        spawnDrops();


        this.turnFinish = false;
        world.getBus().postQueued(new TurnStartEvent(team));
        Logger.logger.info("Start turn for the team {}\n", team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));
        if (this.nbTurn > world.getTeams().size() * 4) {
            Logger.logger.info("Level of the water : {}", world.getMap().getHeightWater());
            if (world.getMap().risingWater(1)) {
                world.getBus().postQueued(new MapUpdateEvent());
            }
        }
        // La Save du premier tour ne marche pas . TODO : Faire sorte qu'elle marche
        Save.setGameWorld(world);
        this.world.getBus().postQueued(new SaveEvent());
    }

    /**
     * Spawn {@link Coin} on the map at random position which can be collect.
     */
    private void spawnCoins() {
        Random random = new Random();
        int coinsToSpawn = 1 + random.nextInt(2);
        int mapWidth = world.getMap().getWidth();
        int mapHeight = world.getMap().getHeight();

        for (int i = 0; i < coinsToSpawn; i++) {
            int x = random.nextInt(mapWidth);
            for (int y = mapHeight - 1; y > 0; y--) {
                if (world.getMap().isSolid(new Position(x, y))) {
                    int coinY = y + 1;
                    Coin coin = new Coin(new Position(x + 0.25, coinY), 50);
                    world.getPhysics().getBodies().add(coin.getBody());
                    break;
                }
            }
        }
    }

    /**
     * Spawn {@link Drop} on the map at random position which can be collect.
     */
    private void spawnDrops() {
        Random random = new Random();
        int dropToSpawn = random.nextInt(2);
        int mapWidth = world.getMap().getWidth();
        int mapHeight = world.getMap().getHeight();

        for (int i = 0; i < dropToSpawn; i++) {
            int x = random.nextInt(mapWidth);
            int y = mapHeight - 1;


            ItemId itemId = null;
            if (random.nextInt(2) == 0) {
                //Weapon Drop
                WeaponId[] weaponIdValues = WeaponId.values();
                itemId = weaponIdValues[random.nextInt(1, weaponIdValues.length - 1)]; // Between 1 and length-1 to remove Gun and None in the choice
            } else {
                // Tool Drop
                ToolId[] toolIdValues = ToolId.values();
                itemId = toolIdValues[random.nextInt(toolIdValues.length - 1)];  // Between 0 and length-1 to remove None in the choice
            }


            Logger.logger.info(" Drop spawn with item : {}", itemId);
            Drop drop = new Drop(new Position(x + 0.25, y), itemId);
            world.queueCommands(drop.addModifier());
            }
    }


    /**
     * Update the current Turn and checks if it finished.
     * Before start a next turn, the turn have to be clean.
     * @param elapsedTime the elapsed time since the last update
     */
    public void updateTurn(double elapsedTime) {
        this.remainingTimeInMs -= (long) elapsedTime;
        if (turnFinish) {
            if (cleanTurn()) {
                this.passNextTurn();
                world.getGameLoop().setSpeedTurn(1);
            } else {
                world.getGameLoop().setSpeedTurn(3);
            }
        } else {
            if (remainingTimeInMs <= 0 || this.turn.getIsEnd()) {
                endTurn();
            } else if (this.turn.teamMaximumShoot()) {
                this.remainingTimeInMs = Math.min(this.remainingTimeInMs, remainingTimeAfterShootInMs);
            }
        }
    }

    /**
     * Ends the current team turn.
     * The current worm is disabled and his hand is cleared.
     * The turn manager enter in state of Turn finished and wait for clean turn
     */
    public void endTurn() {
        this.turn.getTeam().emptyHand();
        Logger.logger.info("End turn - switch Team\n");
        world.getBus().postQueued(new TurnEndEvent());
        this.turnFinish = true;
        this.turn.getTeam().setCurrentWormNull();
        applyStatusEffectsToAllWorms();
    }

    /**
     * apply to all worms the status effect at each end of turn
     */
    private void applyStatusEffectsToAllWorms() {
        for (Team team : world.getTeams()) {
            for (Worm worm : team.getTeamWorms()) {
                List<GameCommand> commands = worm.applyStatusEffects();
                world.queueCommands(commands);
            }
        }
    }

    /**
     * Check if the turn is clean. clean state :
     * <ul>
     *   <li>All {@link Projectile} which need to clear, is clear.</li>
     *   <li>Any alive {@link Worm} is on the ground</li>
     * </ul>
     * @return a boolean if the turn is in a clean state.
     */
    private boolean cleanTurn() {
        for (PhysicsBody body : world.getPhysics().getBodies()) {
            if (body.getEntity() instanceof Projectile projectile) {
                if (projectile.blocksTurnEnd()) return false;
            } else if (body.getEntity() instanceof Worm worm) {
                this.world.getPhysics().removeModifierFor(body, ToolId.JETPACK);
                if (!worm.isOnGround()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tells the current {@link Turn} to close and requests the next team to play.
     */
    private void passNextTurn() {
        this.turn.end();
        world.nextTeamTurn();
    }

    /**
     * Registers and subscribes all game and input event listeners to the world event bus.
     * <p>
     * This allows the turn manager to react to:
     * movement input, weapon/tool selection, coin pickup, worm landing, turn skip, and game stop.
     * <p>
     * Listeners persist across turns because they use dynamic data from {@code TurnManager}.
     */
    private void setupEvents() {
        // TODO - unsubscribe when stopping the gameworld. Note that we can keep these listeners across turns because they use dynamic data

        EventListener<TurnStopEvent> turnStopListener = event -> {
            this.turn.setIsEnd();
        };

        EventListener<TurnSkipEvent> turnskipEventListener = event -> {
            if (remainingTimeInMs < turnDurationInMs - 3000) {
                Logger.logger.info("Turn skipped by team {}", this.turn.getTeam().name);
                endTurn();
            }
        };

        EventListener<MoveInputEvent> moveListener = event -> {
            world.queueCommand(move(this.turn.getWormPlayable(), event.direction()));
        };

        EventListener<GroundCheckCollision> groundCollisionListener = event -> {
            if (turn.getWormPlayable().uuid == event.worm().uuid) {
                Move.jumpToMax();
            }
        };

        EventListener<TurnFinishEvent> turnFinishListener = event -> {
            //TODO : Close the game properly
            world.stop();

        };

        EventListener<WeaponSelectedEvent> weaponSelectedEventEventListener = event -> {
            turn.getTeam().selectWeapon(event.getWeaponId());
        };

        EventListener<ToolSelectedEvent> toolSelectedEventEventListener = event -> {
            turn.getTeam().selectTool(event.getToolId());
        };

        EventListener<CoinCollisionEvent> coinListener = event -> {
            world.queueCommand(w -> {
                Logger.logger.info("Piece ramasse ! + " + event.coin.value + "$");
                event.team.addCredits(event.coin.value);
               world.getPhysics().remove(event.coin.getBody());
            });
        };

        EventListener<DropCollisionEvent> dropListener = event -> {
            world.queueCommand(w -> {
                if (event.drop.itemIdToUnlock instanceof ToolId toolID) {
                    event.team.addAmmo(toolID, event.ammoAmount);
                    Logger.logger.info("Drop ramasse, outil obtenu : " + event.drop.itemIdToUnlock + ", nombre d'utilisation : " + event.ammoAmount + " !");
                } else if (event.drop.itemIdToUnlock instanceof WeaponId weaponID) {
                    event.team.unlockWeapon(weaponID);
                    event.team.addAmmo(weaponID, event.ammoAmount);
                    Logger.logger.info("Drop ramasse, arme obtenu : " + event.drop.itemIdToUnlock + ", nombre de munitions : " + event.ammoAmount + " !");
                }
                world.getPhysics().remove(event.drop.getBody());
            });
        };


        EventListener<ChangeWormsEvent> changeWormDuringTurn = event -> {
            Team currentTeam = turn.getTeam();
            if (currentTeam.getMaxChangeWormTeam() <= 0) {
                Logger.logger.info("It's not possible to change worm playable");
            } else {
                turn.nextWormPlayable();
                currentTeam.oneChangeWorm();
                Logger.logger.info("Change worm playable");
            }
        };

        EventListener<UseInputEvent> useInputEventListener = event -> {
            PhysicsBody wormBody = turn.getWormPlayable().getBody();

            double mouseX = event.pos().getX();
            double mouseY = event.pos().getY();
            double wormX = wormBody.getPos().getX();
            double wormY = wormBody.getPos().getY();

            double centerX = wormX + (wormBody.getWidth() / 2.0);
            double centerY = wormY + (wormBody.getHeight() / 2.0);
            double deltaX = mouseX - centerX;
            double deltaY = (mouseY - centerY);
            double angle = Math.atan2(deltaY, deltaX);

            double dist = 1;
            if (turn.getTeam().getHand() == WeaponId.GASGUN) {
                dist =  6;
            }
            double spawnX = centerX + (Math.cos(angle) * dist);
            double spawnY = centerY + (Math.sin(angle) * dist);

            Position spawnBullet = new Position(spawnX, spawnY);

            if (turn.getTeam().getHand().usesCrosshair()) {
                spawnBullet = new Position(mouseX, world.getMap().getHeight() - 1);
                angle = -Math.PI / 2;
            }

            UseParams params = new UseParams(angle, spawnBullet, Objects.requireNonNull(turn.getWormPlayable()), event.time());

            world.queueCommands(turn.getTeam().use(params));
        };

        world.getBus().subscribe(TurnStopEvent.class, turnStopListener);
        world.getBus().subscribe(TurnSkipEvent.class, turnskipEventListener);

        world.getBus().subscribe(MoveInputEvent.class, moveListener);
        world.getBus().subscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().subscribe(WeaponSelectedEvent.class, weaponSelectedEventEventListener);
        world.getBus().subscribe(ToolSelectedEvent.class, toolSelectedEventEventListener);
        world.getBus().subscribe(UseInputEvent.class, useInputEventListener);
        world.getBus().subscribe(TurnFinishEvent.class, turnFinishListener);
        world.getBus().subscribe(CoinCollisionEvent.class, coinListener);
        world.getBus().subscribe(DropCollisionEvent.class, dropListener);
        world.getBus().subscribe(ChangeWormsEvent.class, changeWormDuringTurn);
    }
}
