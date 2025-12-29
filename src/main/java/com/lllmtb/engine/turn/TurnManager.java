package com.lllmtb.engine.turn;

import java.util.Objects;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnFinishEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.UseParams;
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
        this.nbTurn += 1;
        this.turnFinish = false;
        world.getBus().postQueued(new TurnStartEvent(team));
        Logger.logger.info("Start turn for the team {}\n", team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));
        if (this.nbTurn > world.getTeams().size() * 4) {
            System.out.println(world.getMap().getHeightWater());
            if (world.getMap().risingWater()) {
                world.getBus().postQueued(new MapUpdateEvent());
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
            if (body.getEntity() instanceof Projectile) {
                //TODO : SPECIFY PROJECTILE HERE, which need to be wait and no
                return false;
            }
        }
        return true;
    }

    private void passNextTurn() {
        this.turn.end();
        world.nextTeamTurn();
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

        EventListener<MoveEvent> moveListener = event -> {
            world.queueCommand(move(this.turn.getWormPlayable(), event.direction));
        };

        EventListener<GroundCheckCollision> groundCollisionListener = event -> {
            if (turn.getWormPlayable().uuid == event.worm.uuid) {
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

        groundCollisionListener = event -> {
            if (turn.getWormPlayable().uuid == event.worm.uuid) {
                Move.jumpToMax();
            }
        };

        EventListener<MouseInputEvent> mouseInputEventListener = event -> {
            PhysicsBody wormBody = turn.getWormPlayable().getBody();

            double mouseX = event.getPos().getX();
            double mouseY = event.getPos().getY();
            double wormX = wormBody.getPos().getX();
            double wormY = wormBody.getPos().getY();

            double centerX = wormX + (wormBody.getWidth() / 2.0);
            double centerY = wormY + (wormBody.getHeight() / 2.0);
            double deltaX = mouseX - centerX;
            double deltaY = (mouseY - centerY);
            double angle = Math.atan2(deltaY, deltaX);

            double dist = 1.5;
            double spawnX = centerX + (Math.cos(angle) * dist);
            double spawnY = centerY + (Math.sin(angle) * dist);

            Position spawnBullet = new Position(spawnX, spawnY);
            UseParams params = new UseParams(angle, spawnBullet, Objects.requireNonNull(turn.getWormPlayable()));

            world.queueCommands(turn.getTeam().use(params));
        };

        world.getBus().subscribe(TurnStopEvent.class, turnStopListener);
        world.getBus().subscribe(TurnSkipEvent.class, turnskipEventListener);

        world.getBus().subscribe(MoveEvent.class, moveListener);
        world.getBus().subscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().subscribe(WeaponSelectedEvent.class, weaponSelectedEventEventListener);
        world.getBus().subscribe(MouseInputEvent.class, mouseInputEventListener);
        world.getBus().subscribe(TurnFinishEvent.class, turnFinishListener);
    }
}
