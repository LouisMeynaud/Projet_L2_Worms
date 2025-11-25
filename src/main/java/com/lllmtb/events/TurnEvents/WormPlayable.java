package com.lllmtb.events.TurnEvents;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.worm.Worm;

public class WormPlayable implements Event {
    public final Worm worm;

    public WormPlayable(Worm worm){
        this.worm = worm;
    }
}
