package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;

public class MapUpdateEvent implements Event {

    private final Position center;
    private final int radius;
    private final boolean fullUpdate;


    public MapUpdateEvent(Position center, int radius) {
        this.center = center;
        this.radius = radius;
        this.fullUpdate = false;
    }


    public MapUpdateEvent() {
        this.center = null;
        this.radius = 0;
        this.fullUpdate = true;
    }

    public Position getCenter() {
        return center;
    }
    public int getRadius() {
        return radius;
    }
    public boolean isFullUpdate() {
        return fullUpdate;

    }
}