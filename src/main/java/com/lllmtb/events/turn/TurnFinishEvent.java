package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public class TurnFinishEvent implements Event {
    public final Team winningTeam;

    public TurnFinishEvent(Team team){
        this.winningTeam = team;
    }
}
