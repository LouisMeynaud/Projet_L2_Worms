package com.lllmtb.engine.turn;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;

/**
 * Represents a team turn.
 * A {@code Turn} keeps the active team and checks if the turn must end.
 * Most method of this class is explicit and doesn't need Javadoc.
 */
public class Turn {
    public static final int FIRE_LIMIT_PER_TURN = 3;

    private final Team team;
    private boolean isEnd = false;

    /**
     * Constructor of Turn
     * When the turn starts, it makes sure the current worm is alive.
     * If the worm is not alive, the team switches to the next worm until it finds one is available to play.
     * @param team the {@link Team} playing this turn
     */
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

    /**
     * Ends the current turn.
     * Reset the team's shoot count and worm change count,
     * Move to the next worm for the next Turn.
     */
    public void end() {
        this.team.resetShootCount();
        this.team.resetWormChangeCount();
        this.team.nextWorm();
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setIsEnd() {
        this.isEnd = true;
    }

    /**
     * Checks if the active worm is still alive.
     * If the worm is not alive, the turn is ended.
     */
    public void wormPlayableAlive() {
        if (!this.getWormPlayable().isAlive()) {
            setIsEnd();
        }
    }

    /**
     * Checks if the team has reached the maximum number of shots for this turn.
     * If the team shot has reached this limit, the worm's hand is cleared.
     * @return {@code true} if the team reached the limit, {@code false} otherwise
     */
    public boolean teamMaximumShoot() {
        if (this.team.getShootCount() >= FIRE_LIMIT_PER_TURN) {
            this.team.emptyHand();
            return true;
        }
        return false;
    }

}
