package com.lllmtb.model.item.weapon;

public class UnknownWeaponException extends RuntimeException {
    public final WeaponId id;

    public UnknownWeaponException(WeaponId id) {
        super("No weapon was found for id: " + id);
        this.id = id;
    }
}
