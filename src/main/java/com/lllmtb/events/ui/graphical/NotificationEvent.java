package com.lllmtb.events.ui.graphical;

import com.lllmtb.events.Event;

/**
 * Event representing a notification to be displayed in the UI.
 */
public record NotificationEvent(String message, String imageName) implements Event {
}