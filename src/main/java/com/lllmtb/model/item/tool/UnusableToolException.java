package com.lllmtb.model.item.tool;

import com.lllmtb.model.entity.team.Team;

public class UnusableToolException extends RuntimeException {
    public final ToolId id;
    public final Team player;

    public UnusableToolException(ToolId id, Team player) {
        super("Tool " + id + " cannot be used by " + player);
        this.id = id;
        this.player = player;
    }
}
