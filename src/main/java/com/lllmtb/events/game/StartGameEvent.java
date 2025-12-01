package com.lllmtb.events.game;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.Event;

public class StartGameEvent implements Event {


    private final GameSettings settings;

    public StartGameEvent(GameSettings settings) {
        this.settings = settings;
    }

    public GameSettings getSettings() {
        return settings;
    }
}