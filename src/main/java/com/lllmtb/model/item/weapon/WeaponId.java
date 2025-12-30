package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;

public enum WeaponId implements ItemId {
    GUN,
    BAZOOKA,
    HEALINGBAZOOKA,
    SHOTGUN,
    ELECTRONCANON(false, "blabla"),
    GASGUN,
    BOW,
    GRENADE,
    NONE;

    private final boolean usesCrosshair;
    private final String info;

    WeaponId(boolean usesCrosshair, String info) {
        this.usesCrosshair = usesCrosshair;
        this.info = info;
    }

    WeaponId() {
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
