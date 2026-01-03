package com.lllmtb.engine.turn;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;

public class Turn {
    public static final int FIRE_LIMIT_PER_TURN = 3;

    private final Team team;
    private boolean isEnd = false;

    public Turn(Team team) {
        this.team = team;
        while (!this.getWormPlayable().isAlive()) {
            this.team.nextWorm();
        }
    }

    public Team getTeam() {
        return this.team;
    }

    public Worm getWormPlayable() {
        return team.getCurrentWorm();
    }

    public void nextWormPlayable() {
        do {
            this.team.nextWorm();
        }
        while (!this.getWormPlayable().isAlive());
    }

    public void end() {
        this.team.resetShootCount();
        this.team.nextWorm();
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd() {
        this.isEnd = true;
    }

    public boolean teamMaximumShoot() {
        if (this.team.getShootCount() >= FIRE_LIMIT_PER_TURN) {
            this.team.emptyHand();
            return true;
        }
        return false;
    }

}
