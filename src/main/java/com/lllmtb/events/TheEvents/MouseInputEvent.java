package com.lllmtb.events.TheEvents;

import com.lllmtb.events.Event;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.GraphicsUi.GamePanel.Direction;

public class MouseInputEvent implements Event {
    private Position pos;
    private double time;
    private Direction bouton;
    public MouseInputEvent(Position pos, double time, Direction bouton) {
        this.pos = pos;
        this.time = time;
        this.bouton= bouton;
        System.out.println("click bouton " + this.bouton + ": sec= " + this.time);
    }

}
