package com.lllmtb.events.physics;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.worm.Worm;

public class RemoveWormOfTeam implements Event {

    public final Worm worm;

    public RemoveWormOfTeam(Worm worm) {
        this.worm = worm;
    }
}
