package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;

public enum ToolId implements ItemId {
    PARACHUTE,
    JETPACK,

    CALLSTRIKE(true, "blabla", 0),
    CATCONCRETE(true, "blablabis", 0),
    TOWER(false, "tower info", 3000),
    MINE(false, "mine info", 3000),
    MEDKIT,
    NONE;

    private final boolean usesCrosshair;
    private final String info;
    private final int maxCharge;

    ToolId(boolean usesCrosshair, String info, int maxCharge) {
        this.usesCrosshair = usesCrosshair;
        this.info = info;
        this.maxCharge = maxCharge;
    }

    ToolId() {
        this(false, "", 0);
    }

    @Override
    public boolean usesCrosshair() {
        return usesCrosshair;
    }

    public String getInfo() {
        return this.info;
    }

    @Override
    public int getMaxChargeTime() {
        return this.maxCharge;
    }
}
