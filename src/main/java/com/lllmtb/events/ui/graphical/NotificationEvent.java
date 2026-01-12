package com.lllmtb.events.ui.graphical;

import com.lllmtb.events.Event;
import com.lllmtb.model.entity.team.Team;

/**
 * Event representing a notification to be displayed in the UI.
 */
public record NotificationEvent(String message, String imageName, Team team) implements Event {
    public NotificationEvent(String message, String imageName) {
        this(message, imageName, null);
    }
}