package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.item.tool.ToolId;

public class ToolSelectedEvent implements Event {
    private final ToolId toolId;

    public ToolSelectedEvent(ToolId toolId) {
        this.toolId = toolId;
    }

    public ToolId getToolId() {
        return toolId;
    }
}