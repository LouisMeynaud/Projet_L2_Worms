package com.lllmtb.events.game;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;

public class LoadEvent implements Event {
    public  final String stringPath;
    public LoadEvent(String stringPath) {
        super();
        this.stringPath = stringPath;
    }
}
