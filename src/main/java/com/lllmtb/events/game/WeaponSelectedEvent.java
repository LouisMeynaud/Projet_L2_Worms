package com.lllmtb.events.game;

import com.lllmtb.events.Event;
import com.lllmtb.model.item.weapon.WeaponId;


public record WeaponSelectedEvent(WeaponId weaponId) implements Event {
}