package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemRegistry;

public final class WeaponRegistry extends ItemRegistry<WeaponId, Weapon> {

    public WeaponRegistry() {
        super(WeaponId.class);

        this.register(WeaponId.GUN, new GunWeapon());
        this.register(WeaponId.BAZOOKA, new BazookaWeapon());
        this.register(WeaponId.HEALINGBAZOOKA, new HealingBazookaWeapon());
        this.register(WeaponId.SHOTGUN, new ShotGunWeapon());
        this.register(WeaponId.ELECTRONCANON, new ElectronCanonWeapon());
    }
}
