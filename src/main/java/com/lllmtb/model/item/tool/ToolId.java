package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;

public enum ToolId implements ItemId {
    PARACHUTE(false, "Deploys a canopy for a safe, wind-affected descent.", 0),
    JETPACK(false, "Fly freely around the map. Mind the fuel gauge!", 0),
    CALLSTRIKE(true, "Call in a devastating airstrike of missiles on a targeted area.", 0),
    CATCONCRETE(true, "Summons a villainous stone cat to smash through all terrain beneath it.", 0),
    TOWER(false, "Deploys a defensive turret to intercept enemies approaching you in future turns.", 3000),
    MINE(false, "A volatile trap that detonates instantly if anyone dares to step on it.", 3000),
    GRENADE(false, "A timed bomb that bounces off terrain before exploding.", 4000),
    MEDKIT(false, "Restores a portion of the active worm's health.", 0),
    TRAP(false, "A throwing trap that releases poison", 3000),
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


    /**
     * Gets the information description of the tool.
     * @return Description string of the tool.
     */
    public String getInfo() {
        return this.info;
    }

    /**
     * Gets the maximum charge time for the tool.
     * @return Maximum charge time in milliseconds.
     */
    @Override
    public int getMaxChargeTime() {
        return this.maxCharge;
    }
}
