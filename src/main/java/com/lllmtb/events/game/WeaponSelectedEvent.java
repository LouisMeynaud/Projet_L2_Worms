package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.item.weapon.WeaponId;


public class WeaponSelectedEvent implements Event {


    private final WeaponId weaponId;

    public WeaponSelectedEvent(WeaponId weaponId) {
        this.weaponId = weaponId;
    }
    public WeaponId getWeaponId() {
        return weaponId;
    }
}