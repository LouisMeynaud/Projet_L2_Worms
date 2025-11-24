package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.team.Team;

public class UnusableWeaponException extends RuntimeException {
    public final WeaponId id;
    public final Team player;

    public UnusableWeaponException(WeaponId id, Team player) {
        super("Weapon " + id + " cannot be used by " + player);
        this.id = id;
        this.player = player;
    }
}
