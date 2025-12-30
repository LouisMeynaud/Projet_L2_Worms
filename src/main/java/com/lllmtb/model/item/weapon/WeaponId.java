package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;

public enum WeaponId implements ItemId {
    GUN,
    BAZOOKA,
    HEALINGBAZOOKA,
    SHOTGUN,
    ELECTRONCANON,
    GASGUN,
    BOW,
    GRENADE,
    NONE;

    private final boolean usesCrosshair;

    WeaponId(boolean usesCrosshair) {
        this.usesCrosshair = usesCrosshair;
    }

    WeaponId() {
        this(false);
    }

    @Override
    public boolean usesCrosshair() {
        return usesCrosshair;
    }
}
