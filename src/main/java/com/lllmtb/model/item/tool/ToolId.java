package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;

public enum ToolId implements ItemId {
    PARACHUTE,
    JETPACK,
    CALLSTRIKE(true),
    CATCONCRETE(true),
    NONE;

    private final boolean usesCrosshair;

    ToolId(boolean usesCrosshair) {
        this.usesCrosshair = usesCrosshair;
    }

    ToolId() {
        this(false);
    }

    @Override
    public boolean usesCrosshair() {
        return usesCrosshair;
    }
}
