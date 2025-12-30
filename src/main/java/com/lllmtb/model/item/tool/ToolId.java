package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;

public enum ToolId implements ItemId {
    PARACHUTE,
    JETPACK,
    CALLSTRIKE(true, "blabla"),
    CATCONCRETE(true, "blablabis"),
    TOWER,
    MINE,
    NONE;

    private final boolean usesCrosshair;
    private final String info;

    ToolId(boolean usesCrosshair, String info) {
        this.usesCrosshair = usesCrosshair;
        this.info = info;
    }

    ToolId() {
        this(false, "");
    }

    @Override
    public boolean usesCrosshair() {
        return usesCrosshair;
    }

    public String getInfo() {
        return this.info;
    }
}
