package com.lllmtb.engine;

import com.lllmtb.events.EventListener;
import com.lllmtb.events.TurnEvents.TurnEndEvent;
import com.lllmtb.events.TurnEvents.TurnStarEvent;
import com.lllmtb.events.TurnEvents.TurnStopEvent;
import com.lllmtb.events.TurnEvents.WormPlayable;
import com.lllmtb.events.graphicsUi.MoveEvent;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Move;

import static com.lllmtb.physics.Move.move;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;
    private EventListener<MoveEvent> listener = event -> {
        move(this.turn.getWormPlayable(), event.direction);
        System.out.println("Move");
    };
    private EventListener<GroundCheckCollision> listenerGround = event -> {
        if (turn.getWormPlayable().uuid == event.worm.uuid) {
            Move.jumpToMax();
            System.out.println("Reset Jump");
        };
    };



    private long startTime;
    private final long tourDurationInMs;

    public TurnManager(GameWorld world, long tourDuration) {
        this.world = world;
        this.tourDurationInMs = tourDuration;
        this.init();
    }

    public GameWorld getWorld(){
        return this.world;
    }

    public void init(){
        world.getBus().subscribe(TurnStopEvent.class, event -> {this.turn.setIsEnd();});
    }

    public void startTurn(Team team) {
        startTime = System.currentTimeMillis();
        this.turn = new Turn(team);
        world.getBus().postQueued(new TurnStarEvent(team));
        System.out.println("Start turn for the team " + team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));
        world.getBus().subscribe(MoveEvent.class, listener);
        world.getBus().subscribe(GroundCheckCollision.class, listenerGround);
    }

    public void updateTurn() {
        long now = System.currentTimeMillis();
        // Timer
        if (now - startTime >= tourDurationInMs) {
            endTurn();
        }
        if(this.turn.getIsEnd()){
            endTurn();
        }
    }

    private void endTurn() {
        this.turn.end();
        System.out.println("End turn - switch Team");
        world.getBus().unsubscribe(MoveEvent.class, listener);
        world.getBus().unsubscribe(GroundCheckCollision.class, listenerGround);
        world.getBus().postQueued(new TurnEndEvent());
        world.nextTeamTurn();

    }
}
