package com.lllmtb.model.item.weapon;

import com.lllmtb.model.item.IItems;

public abstract class Weapon implements IItems {

    private final int projectileId;

    public Weapon(int projectileId) {
        this.projectileId = projectileId;
    }

    public int getProjectileId() {
        return projectileId;
    }
}