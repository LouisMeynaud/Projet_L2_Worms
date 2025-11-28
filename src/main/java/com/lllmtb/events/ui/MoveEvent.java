package com.lllmtb.events.ui;

import com.lllmtb.events.Event;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public class MoveEvent implements Event {
    public final Direction direction;


    public MoveEvent(Direction direction) {
        this.direction = direction;
    }

}
