package com.lllmtb.engine.turn;

import java.util.List;
import java.util.Objects;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.turn.TurnStarEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Move;
import static com.lllmtb.physics.Move.move;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;
    private EventListener<MoveEvent> moveListener;
    private EventListener<WeaponSelectedEvent> weaponSelectedEventEventListener;
    private EventListener<MouseInputEvent> mouseInputEventListener;
    private EventListener<TurnSkipEvent> turnskipEventListener;
    private EventListener<GroundCheckCollision> groundCollisionListener = event -> {
        if (turn.getWormPlayable().uuid == event.worm.uuid) {
            Move.jumpToMax();
        }
    };

    private long startTime;
    private final long turnDurationInMs;
    private long remainingTimeInMs;

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
        world.getBus().subscribe(TurnStopEvent.class, event -> {
            this.turn.setIsEnd();
        });
    }

    public void startTurn(Team team) {
        this.remainingTimeInMs = turnDurationInMs;
        startTime = System.currentTimeMillis();
        this.turn = new Turn(team);
        world.getBus().postQueued(new TurnStarEvent(team));
        Logger.logger.info("Start turn for the team {}\n", team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));

        setupEvent();
    }

    public void updateTurn() {
        long now = System.currentTimeMillis();
        long timeElapsed = now - startTime;
        this.remainingTimeInMs = turnDurationInMs - timeElapsed;
        // Timer
        if (now - startTime >= turnDurationInMs) {
            endTurn();
        }
        if (this.turn.getIsEnd()) {
            endTurn();
        }
    }

    private void endTurn() {
        this.turn.end();
        Logger.logger.info("End turn - switch Team\n");
        world.getBus().unsubscribe(MoveEvent.class, moveListener);
        world.getBus().unsubscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().postQueued(new TurnEndEvent());
        world.getBus().unsubscribe(TurnSkipEvent.class, turnskipEventListener);
        world.nextTeamTurn();

    }

    public Turn getTurn() {
        return turn;
    }

    private void setupEvent() {

        moveListener = event -> {
            world.queueCommand(move(this.turn.getWormPlayable(), event.direction));
        };

        weaponSelectedEventEventListener = event -> {
            turn.getTeam().selectWeapon(event.getWeaponId());
        };

        mouseInputEventListener = event -> {
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

            List<GameCommand> cmds = turn.getTeam().use(params);
            for (GameCommand c : cmds) {
                world.queueCommand(c);
            }
        };

        turnskipEventListener = event -> {
            if (remainingTimeInMs < turnDurationInMs - 3000) {
                Logger.logger.info("Turn skipped by team {}", this.turn.getTeam().name);
            endTurn();
        }
        };

        world.getBus().subscribe(MoveEvent.class, moveListener);
        world.getBus().subscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().subscribe(WeaponSelectedEvent.class, weaponSelectedEventEventListener);
        world.getBus().subscribe(MouseInputEvent.class, mouseInputEventListener);
        world.getBus().subscribe(TurnSkipEvent.class, turnskipEventListener);
    }
}
