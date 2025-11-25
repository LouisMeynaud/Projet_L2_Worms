package com.lllmtb.engine;

import com.lllmtb.events.TurnEvents.TurnEndEvent;
import com.lllmtb.events.TurnEvents.TurnStarEvent;
import com.lllmtb.events.TurnEvents.TurnStopEvent;
import com.lllmtb.events.TurnEvents.WormPlayable;
import com.lllmtb.model.entity.team.Team;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;

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
        world.getBus().postQueued(new TurnEndEvent());
        System.out.println("End turn - switch Team");
        world.nextTeamTurn();
    }
}
