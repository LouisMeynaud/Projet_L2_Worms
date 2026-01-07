package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.item.tool.ToolId;

public record ToolSelectedEvent(ToolId toolId) implements Event {
}