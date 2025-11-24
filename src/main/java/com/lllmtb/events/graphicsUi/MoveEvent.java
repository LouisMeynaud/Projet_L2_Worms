package com.lllmtb.events.TheEvents;

import com.lllmtb.events.Event;
import com.lllmtb.ui.GraphicsUi.GamePanel.Direction;

public class MoveEvent implements Event {
    public final Direction direction;


    public MoveEvent(Direction direction) {
        this.direction = direction;
    }

}
