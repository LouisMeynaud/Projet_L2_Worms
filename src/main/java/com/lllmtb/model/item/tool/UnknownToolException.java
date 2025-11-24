package com.lllmtb.model.item.tool;

public class UnknownToolException extends RuntimeException {
    public final ToolId id;

    public UnknownToolException(ToolId id) {
        super("No tool was found for id: " + id);
        this.id = id;
    }
}
