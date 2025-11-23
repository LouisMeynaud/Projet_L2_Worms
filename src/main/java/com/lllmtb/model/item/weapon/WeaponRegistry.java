package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.ItemRegistry;

public final class WeaponRegistry extends ItemRegistry<WeaponId, Weapon> {

    public WeaponRegistry() {
        super(WeaponId.class);

        this.registry.put(WeaponId.GUN, new GunWeapon());
        this.registry.put(WeaponId.BAZOOKA, new BazookaWeapon());
    }
}
