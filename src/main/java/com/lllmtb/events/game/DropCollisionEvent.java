package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Drop;
import com.lllmtb.model.entity.team.Team;

import java.util.Random;

public class DropCollisionEvent implements Event {
    public final Team team;
    public final Drop drop;
    private final Random random = new Random();
    public final int ammoAmount = random.nextInt(1, 6);

    public DropCollisionEvent(Drop drop, Team team) {
        this.team = team;
        this.drop = drop;

    }
}
