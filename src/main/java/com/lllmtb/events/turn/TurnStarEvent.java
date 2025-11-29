package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public class TurnStarEvent implements Event {
    public final Team team;

    public TurnStarEvent(Team team){
        this.team = team;
    }
}
