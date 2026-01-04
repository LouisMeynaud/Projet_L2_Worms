package com.lllmtb.events.turn;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.worm.Worm;

public record WormPlayable(Worm worm) implements Event {
}
