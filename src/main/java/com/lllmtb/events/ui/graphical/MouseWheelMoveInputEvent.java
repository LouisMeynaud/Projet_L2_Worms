package com.lllmtb.events.ui.graphical;

import com.lllmtb.events.Event;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public record MouseWheelMoveInputEvent(Direction direction) implements Event {
}
