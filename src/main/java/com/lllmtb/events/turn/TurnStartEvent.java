package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

public record TurnStartEvent(Team team) implements Event {
}
