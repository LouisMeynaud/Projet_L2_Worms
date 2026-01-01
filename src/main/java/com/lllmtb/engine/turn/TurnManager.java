package com.lllmtb.engine.turn;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.save.Save;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.game.CoinCollisionEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.ToolSelectedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
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
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Move;
import static com.lllmtb.physics.Move.move;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;

    private final long turnDurationInMs;
    private long remainingTimeInMs;
    private int nbTurn;
    private boolean turnFinish = false;

    public TurnManager(GameWorld world, long turnDuration) {
        this.world = world;
        this.turnDurationInMs = 1000 * turnDuration;
        this.init();
    }

    public GameWorld getWorld() {
        return this.world;
    }

    public long getTurnDuration() {
        return this.turnDurationInMs;
    }

    public long getRemainingTime() {
        return this.remainingTimeInMs;
    }

    public void init() {
        setupEvents();
    }

    public void startTurn(Team team) {
        this.remainingTimeInMs = turnDurationInMs;
        this.turn = new Turn(team);
        try {
            Save.saveAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.nbTurn += 1;

        //economy passive income at the start of the turn
        int passiveIncome = 150; //ajout de beaucoup d'argent pour pouvoir tester les arme et le shop
        team.addCredits(passiveIncome);
        Logger.logger.info("l'equipe : " + team.name + "gagne :" + passiveIncome + "pour le debut du tour");
        spawnCoins();


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
    }

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

    public void updateTurn(double elapsedTime) {
        this.remainingTimeInMs -= (long) elapsedTime;
        if (turnFinish) {
            if (cleanTurn()) {
                this.passNextTurn();
                world.getGameLoop().setSpeedTurn(1);
            } else {
                world.getGameLoop().setSpeedTurn(5);
            }
        } else {
            if (remainingTimeInMs <= 0) {
                endTurn();
            } else if (this.turn.getIsEnd()) {
                endTurn();
            }
        }
    }

    public void endTurn() {
        this.turn.getTeam().emptyHand();
        Logger.logger.info("End turn - switch Team\n");
        world.getBus().postQueued(new TurnEndEvent());
        this.turnFinish = true;
        this.turn.getTeam().setCurrentWormNull();
    }

    private boolean cleanTurn() {
        for (PhysicsBody body : world.getPhysics().getBodies()) {
            if (body.getEntity() instanceof Projectile projectile) {
                if (projectile.blocksTurnEnd()) return false;
            }
        }
        return true;
    }

    private void passNextTurn() {
        this.turn.end();
        world.nextTeamTurn();
        Save.setGameWorld(world);

    }

    public Turn getTurn() {
        return turn;
    }

    public int getNbTurn() {
        return this.nbTurn;
    }

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

        EventListener<ChangeWormsEvent> changeWormDuringTurn = event -> {
            turn.nextWormPlayable();
            Logger.logger.info("Change worm playable");
            //TODO : PASS IT TO DEBUG
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
        world.getBus().subscribe(ChangeWormsEvent.class, changeWormDuringTurn);
    }
}
