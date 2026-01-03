package com.lllmtb.events.ui.graphical;

import com.lllmtb.events.Event;

/**
 * Event representing a notification to be displayed in the UI.
 */
public class NotificationEvent implements Event {

    private final String message;
    private final String imageName;

    public NotificationEvent(String message, String imageName) {
        this.message = message;
        this.imageName = imageName;
    }

    public String getMessage() {
        return message;
    }

    public String getImageName() {
        return imageName;
    }
}