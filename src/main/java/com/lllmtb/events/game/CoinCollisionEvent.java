package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.team.Team;

public record CoinCollisionEvent(Coin coin, Team team) implements Event {
}