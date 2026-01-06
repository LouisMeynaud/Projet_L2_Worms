package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;

public class MapUpdateEvent implements Event {
    private final Position center;
    private final int radius;
    private final boolean isFullUpdate;

    public MapUpdateEvent(Position center, int radius) {
        this.center = center;
        this.radius = radius;
        this.isFullUpdate = false;
    }

    public MapUpdateEvent() {
        this.center = null;
        this.radius = 0;
        this.isFullUpdate = true;
    }

    public Position center() {
        return center;
    }

    public int radius() {
        return radius;
    }

    public boolean isFullUpdate() {
        return isFullUpdate;
    }
}