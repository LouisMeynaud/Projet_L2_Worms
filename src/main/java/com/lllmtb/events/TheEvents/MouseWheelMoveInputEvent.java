package com.lllmtb.events.TheEvents;

import com.lllmtb.events.Event;
import com.lllmtb.ui.GraphicsUi.GamePanel.Direction;

public class MouseWheelMoveInputEvent implements Event {
    public final Direction direction;
    public MouseWheelMoveInputEvent( Direction direction) {
        this.direction = direction;
        System.out.println("Roulement vers le " + this.direction);
    }
}
