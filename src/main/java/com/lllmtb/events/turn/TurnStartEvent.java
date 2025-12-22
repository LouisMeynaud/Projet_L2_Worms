package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public class TurnStartEvent implements Event {
    public final Team team;

    public TurnStartEvent(Team team) {
        this.team = team;
    }
}
