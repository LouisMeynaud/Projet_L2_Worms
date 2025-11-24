package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.worm.Worm;

public class UnusableWeaponException extends RuntimeException {
    public final WeaponId id;
    public final Worm owner;

    public UnusableWeaponException(WeaponId id, Worm owner) {
        super("Weapon " + id + " cannot be used by " + owner);
        this.id = id;
        this.owner = owner;
    }
}
