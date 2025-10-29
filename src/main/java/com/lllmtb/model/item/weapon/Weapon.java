package com.lllmtb.model.item.weapon;
import com.lllmtb.model.item.IItems;

public abstract class Weapon implements IItems {

    private int ammunations;
    private boolean unlocked;

    public Weapon (int ammunations, boolean unlocked) {
        this.ammunations = ammunations;
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
    abstract public void use(Entity e, double angle);
}