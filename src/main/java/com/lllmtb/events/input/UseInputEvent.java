package com.lllmtb.events.input;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;

public record UseInputEvent(Position pos, double time) implements Event {
}
