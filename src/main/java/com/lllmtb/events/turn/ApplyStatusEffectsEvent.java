package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

/**
 * Event fired at the end of a turn to apply status effects to worms.
 */
public class ApplyStatusEffectsEvent implements Event {

    /**
     * The team whose worms should have status effects applied.
     */
    public final Team team;

    /**
     * Constructor for ApplyStatusEffectsEvent
     * @param team the team whose worms should have effects applied
     */
    public ApplyStatusEffectsEvent(Team team) {
        this.team = team;
    }
}

