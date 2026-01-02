package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;

public enum WeaponId implements ItemId {
    GUN(false, "A standard handgun for reliable, direct fire.", 0),
    BAZOOKA(false, "Fires a heavy, explosive rocket.", 1000),
    HEALINGBAZOOKA(false, "Fires a medical rocket that heals allies in the blast radius.", 1000),
    SHOTGUN(false, "Fires a spread of pellets, devastating at close range.", 0),
    ELECTRONCANON(false, "Fires an electron beam that bounces from enemy to enemy.", 0),
    GASGUN(false, "Releases a devastating chemical cloud.", 0),
    BOW(false, "A devastating bow, effective at both short and long range.", 3000),
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
    /**
     * Gets the information description of the weapon.
     * @return Description string of the weapon.
     */
    public String getInfo() {
        return this.info;
    }

    @Override
    public int getMaxChargeTime() {
        return this.maxCharge;
    }
}
