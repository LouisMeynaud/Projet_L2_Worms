package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.team.Team;

public class LockedWeaponException extends RuntimeException {
    public final WeaponId id;
    public final Team player;

    public LockedWeaponException(WeaponId id, Team player) {
        super("Weapon " + id + " is locked for worm " + player);
        this.id = id;
        this.player = player;
    }
}
