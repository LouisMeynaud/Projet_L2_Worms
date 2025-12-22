package com.lllmtb.events.input;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public record UseInputEvent(Position pos, double time) implements Event {
}
