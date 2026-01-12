package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.ShootMode;

public enum WeaponId implements ItemId {
    GUN("Un pistolet standard pour un tir direct et fiable."),
    BASEBALLBAT(ShootMode.NONE, "Une arme de mêlée qui repousse les entités proches.", 1000),
    BAZOOKA("Tire une roquette lourde et explosive.", 1000),
    HEALINGBAZOOKA("Tire une roquette qui soigne les alliés dans le rayon d'action de l'explosion.", 1000),
    SHOTGUN("Tire une rafale de plombs, dévastatrice à courte portée."),
    ELECTRONCANON("Tire un faisceau d'électrons qui rebondit d'un ennemi à l'autre."),
    GASGUN("Libère un nuage chimique dévastateur."),
    BOW("Un arc dévastateur, efficace à courte et longue distance.", 3000),
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
