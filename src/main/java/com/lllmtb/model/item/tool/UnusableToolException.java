package com.lllmtb.model.item.tool;

import com.lllmtb.model.entity.worm.Worm;

public class UnusableToolException extends RuntimeException {
    public final ToolId id;
    public final Worm owner;

    public UnusableToolException(ToolId id, Worm owner) {
        super("Tool " + id + " cannot be used by " + owner);
        this.id = id;
        this.owner = owner;
    }
}
