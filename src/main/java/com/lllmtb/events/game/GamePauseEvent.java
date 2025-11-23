package com.lllmtb.events.TheEvents;

import com.lllmtb.events.Event;


public class GamePauseEvent implements Event{
    public final boolean isPaused;

    public GamePauseEvent(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
