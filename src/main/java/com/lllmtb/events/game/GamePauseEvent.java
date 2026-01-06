package com.lllmtb.events.game;

import com.lllmtb.events.Event;

public record GamePauseEvent(boolean isPaused) implements Event {
}
