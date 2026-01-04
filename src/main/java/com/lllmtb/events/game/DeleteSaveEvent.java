package com.lllmtb.events.game;

import com.lllmtb.events.Event;

public class DeleteSaveEvent implements Event {

    private final String saveName;

    public DeleteSaveEvent(String saveName) {
        this.saveName = saveName;
    }

    public String getSaveName() {
        return saveName;
    }
}