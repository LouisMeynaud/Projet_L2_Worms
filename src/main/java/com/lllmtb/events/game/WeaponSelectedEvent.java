package com.lllmtb.events.game;

import com.lllmtb.events.Event; 
import com.lllmtb.model.item.weapon.WeaponId;


public class WeaponSelectedEvent implements Event {
    
   
    public final WeaponId weaponId;

    public WeaponSelectedEvent(WeaponId weaponId) {
        this.weaponId = weaponId;
        System.out.println(weaponId + "choisis");
    }
}