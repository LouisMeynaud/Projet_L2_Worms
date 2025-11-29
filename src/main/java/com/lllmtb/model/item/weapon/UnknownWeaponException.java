package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.UnknownItemException;

public class UnknownWeaponException extends UnknownItemException {

    public UnknownWeaponException(WeaponId id) {
        super(id);
    }
}
