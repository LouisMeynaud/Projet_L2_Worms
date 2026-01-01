package com.lllmtb.events.ui.graphical;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public class ScreenEndGameEvent implements Event {
    private Team team;
    private GameWorld world;

    public ScreenEndGameEvent(Team team, GameWorld world) {
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public GameWorld getWorld() {
        return this.world;
    }
}
