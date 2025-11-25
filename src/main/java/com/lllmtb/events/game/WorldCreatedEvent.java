package com.lllmtb.events.game;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;

public class WorldCreatedEvent implements Event {
    private final GameWorld world;

    public WorldCreatedEvent(GameWorld world) {
        this.world = world;
    }

    public GameWorld getWorld() {
        return world;
    }
}
