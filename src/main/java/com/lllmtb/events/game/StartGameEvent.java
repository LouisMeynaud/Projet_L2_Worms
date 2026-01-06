package com.lllmtb.events.game;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.Event;

public record StartGameEvent(GameSettings settings) implements Event {
}