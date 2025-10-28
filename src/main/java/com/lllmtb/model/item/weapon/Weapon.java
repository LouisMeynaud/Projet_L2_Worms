package com.lllmtb.model.item.weapon;
import com.lllmtb.model.item.IItems;

public abstract class Weapon implements IItems {

    private int ammunations;
    private final Projectile projectileType;
    private boolean unlocked;

    public Weapon (int ammunations, Projectile type, boolean unlocked) {
        this.ammunations = ammunations;
        this.projectileType = type;
        this.unlocked = unlocked;
    }

    @Override
    public boolean inInventory() {
        return unlocked;
    }

    @Override
    public boolean useable() {
        return this.inInventory() && this.ammunations > 0;
    }

    @Override
    public void oneUse() {
        if (this.useable()) {
            this.ammunations--;
        } else {
            throw new IllegalArgumentException("You can't use this weapon!");
        }
    }
}