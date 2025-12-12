package com.lllmtb.engine.turn;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;

public class Turn {
    private final Team team;
    private boolean isEnd = false;

    public Turn(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public Worm getWormPlayable() {
        return team.getCurrentWorm();
    }

    public void end() {
        do {
            this.team.nextWorm();
        }
        while(!this.getWormPlayable().isAlive());
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd() {
        this.isEnd = true;
    }
}
