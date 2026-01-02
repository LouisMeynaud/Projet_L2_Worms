package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.ShootMode;

public enum ToolId implements ItemId {
    PARACHUTE(ShootMode.NONE, "Deploys a canopy for a safe, wind-affected descent."),
    JETPACK(ShootMode.NONE, "Fly freely around the map. Mind the fuel gauge!"),
    CALLSTRIKE(ShootMode.CROSSHAIR, "Call in a devastating airstrike of missiles on a targeted area.", 0),
    CATCONCRETE(ShootMode.CROSSHAIR, "Summons a villainous stone cat to smash through all terrain beneath it.", 0),
    TOWER("Deploys a defensive turret to intercept enemies approaching you in future turns.", 3000),
    MINE("A volatile trap that detonates instantly if anyone dares to step on it.", 3000),
    GRENADE("A timed bomb that bounces off terrain before exploding.", 4000),
    MEDKIT(ShootMode.NONE, "Restores a portion of the active worm's health."),
    NONE;

    private final ShootMode shootMode;
    private final String info;
    private final int maxCharge;

    ToolId(ShootMode shootMode, String info, int maxCharge) {
        this.shootMode = shootMode;
        this.info = info;
        this.maxCharge = maxCharge;
    }

    ToolId(ShootMode shootMode, String info) {
        this(shootMode, info, 0);
    }

    ToolId(String info, int maxCharge) {
        this(ShootMode.AIM, info, maxCharge);
    }

    ToolId(String info) {
        this(ShootMode.AIM, info, 0);
    }

    ToolId() {
        this(ShootMode.AIM, "", 0);
    }

    @Override
    public ShootMode getShootMode() {
        return shootMode;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public int getMaxChargeTime() {
        return this.maxCharge;
    }
}
