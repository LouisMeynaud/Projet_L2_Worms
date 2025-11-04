package com.lllmtb.model.item.tool;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;

public abstract class Tool implements IItems {

    private int ammunations;
    private boolean unlocked;

    public Tool (int ammunations, boolean unlocked) {
        this.ammunations = ammunations;
        this.unlocked = unlocked;
    }

    @Override
    public int getAmmunations() {
        return this.ammunations;
    }

    @Override
    public boolean inInventory() {
        return unlocked;
    }

    @Override
    public boolean usable() {
        return this.inInventory() && this.ammunations > 0;
    }

    @Override
    abstract public void use(Entity e, double angle);

    @Override
    public void unlockItem() {
        this.unlocked = true;
    }

    @Override
    public void addAmmo(int n) {
        if (n > 0) {
            this.ammunations += n;
        }
    }
}