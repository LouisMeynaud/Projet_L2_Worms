package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;

public class ExplosionEvent implements Event {
    private final Position position;
    private final int radius;
    private final String animationKey;

    public ExplosionEvent(Position position, int radius, String animationKey) {
        this.position = position;
        this.radius = radius;
        this.animationKey = animationKey;
    }

    public Position getPosition() {
        return position;
    }

    public int getRadius() {
        return radius;
    }

    public String getAnimationKey() {
        return animationKey;
    }
}