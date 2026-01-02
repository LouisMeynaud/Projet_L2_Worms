package com.lllmtb.events.ui.graphical;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public class ScreenEndGameEvent implements Event {
    private Team team;

    public ScreenEndGameEvent(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

}
