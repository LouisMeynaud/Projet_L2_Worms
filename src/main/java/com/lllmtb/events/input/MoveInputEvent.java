package com.lllmtb.events.input;

import com.lllmtb.events.Event;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public record MoveInputEvent(Direction direction) implements Event {
}
