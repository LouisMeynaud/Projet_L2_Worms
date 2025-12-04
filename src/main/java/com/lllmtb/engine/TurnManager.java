package com.lllmtb.engine;

import com.lllmtb.events.EventListener;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnStarEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Move;

import static com.lllmtb.physics.Move.move;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;

    private EventListener<MoveEvent> moveListener;
    private EventListener<GroundCheckCollision> groundCollisionListener = event -> {
        if (turn.getWormPlayable().uuid == event.worm.uuid) {
            Move.jumpToMax();
        }
    };

    private long startTime;
    private final long turnDurationInMs;

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

    public void init() {
        world.getBus().subscribe(TurnStopEvent.class, event -> {
            this.turn.setIsEnd();
        });
    }

    public void startTurn(Team team) {
        startTime = System.currentTimeMillis();
        this.turn = new Turn(team);
        Save.setGameWorld(world);
        world.getBus().postQueued(new TurnStarEvent(team));
        System.out.println("Start turn for the team " + team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));

        moveListener = event -> {
            world.queueCommand(move(this.turn.getWormPlayable(), event.direction));
            System.out.println("Move");
        };
        world.getBus().subscribe(MoveEvent.class, moveListener);

        world.getBus().subscribe(GroundCheckCollision.class, groundCollisionListener);
    }

    public void updateTurn() {
        long now = System.currentTimeMillis();
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
        System.out.println("End turn - switch Team");
        world.getBus().unsubscribe(MoveEvent.class, moveListener);
        world.getBus().unsubscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().postQueued(new TurnEndEvent());
        world.nextTeamTurn();

    }
}
