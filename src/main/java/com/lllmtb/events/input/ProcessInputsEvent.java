package com.lllmtb.events.input;

import com.lllmtb.events.Event;

/**
 * Event triggered every game tick to process input from the player.
 * Subscribers to this event should send appropriate movement/action events based on currently pressed keys.
 */
public class ProcessInputsEvent implements Event {
    // This event serves as a signal to process inputs during the game loop
}

