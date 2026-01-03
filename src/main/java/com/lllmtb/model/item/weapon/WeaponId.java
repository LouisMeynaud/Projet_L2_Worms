package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.ShootMode;

public enum WeaponId implements ItemId {
    GUN("A standard handgun for reliable, direct fire."),
    BASEBALLBAT(ShootMode.NONE, "A melee weapon that pushes away nearby entities.", 1000),
    BAZOOKA("Fires a heavy, explosive rocket.", 1000),
    HEALINGBAZOOKA("Fires a medical rocket that heals allies in the blast radius.", 1000),
    SHOTGUN("Fires a spread of pellets, devastating at close range."),
    ELECTRONCANON("Fires an electron beam that bounces from enemy to enemy."),
    GASGUN("Releases a devastating chemical cloud."),
    BOW("A devastating bow, effective at both short and long range.", 3000),
    NONE;

    private final ShootMode shootMode;
    private final String info;
    private final int maxCharge;

    WeaponId(ShootMode shootMode, String info, int maxCharge) {
        this.shootMode = shootMode;
        this.info = info;
        this.maxCharge = maxCharge;
    }

    WeaponId(ShootMode shootMode, String info) {
        this(shootMode, info, 0);
    }

    WeaponId(String info, int maxCharge) {
        this(ShootMode.AIM, info, maxCharge);
    }

    WeaponId(String info) {
        this(ShootMode.AIM, info, 0);
    }

    WeaponId() {
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
