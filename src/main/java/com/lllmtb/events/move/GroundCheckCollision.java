package com.lllmtb.events.move;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.Entity;
//import com.lllmtb.model.entity.worm.Worm;

public class GroundCheckCollision  implements Event {
    public final Entity worm;
    public GroundCheckCollision(Entity worm) {
        this.worm = worm;
    }
}
