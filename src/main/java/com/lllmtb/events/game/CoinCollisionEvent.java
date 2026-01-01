package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.team.Team;

public class CoinCollisionEvent implements Event {
    public final Coin coin;
    public final Team team;

    public CoinCollisionEvent(Coin coin, Team team) {
        this.coin = coin;
        this.team = team;
    }
}