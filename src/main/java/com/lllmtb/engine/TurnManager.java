package com.lllmtb.engine;

import java.util.EventListener;

import com.lllmtb.events.TurnEvents.TurnEndEvent;
import com.lllmtb.events.TurnEvents.TurnStarEvent;
import com.lllmtb.events.TurnEvents.WormPlayable;
import com.lllmtb.model.entity.team.Team;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;

    private long startTime;
    private static final long tourDurationInMs = 10_000;

    public TurnManager(GameWorld world) {
        this.world = world;
        this.init();
    }

    public void init(){
        world.getBus().subscribe(TurnEndEvent.class, event -> {this.turn.setIsEnd();});
    }

    public void startTurn(Team team) {
        startTime = System.currentTimeMillis();
        this.turn = new Turn(team);
        world.getBus().postQueued(new TurnStarEvent(team));
        System.out.println("Start turn for the team " + team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));
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
        world.nextTeamTurn();
    }
}
