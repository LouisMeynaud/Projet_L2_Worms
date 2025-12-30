package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;

public enum WeaponId implements ItemId {
    GUN,
    BAZOOKA,
    HEALINGBAZOOKA,
    SHOTGUN,
    ELECTRONCANON(false, "blabla", 0),
    GASGUN,
    BOW(false, "blablabis", 3000),
    GRENADE,
    NONE;

    private final boolean usesCrosshair;
    private final String info;
    private final int maxCharge;

    WeaponId(boolean usesCrosshair, String info, int maxCharge) {
        this.usesCrosshair = usesCrosshair;
        this.info = info;
        this.maxCharge = maxCharge;
    }

    WeaponId() {
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
