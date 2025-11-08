package com.lllmtb.model.item.tool;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;

public abstract class Tool implements IItems {

    private int ammunations;

    public Tool (int ammunations) {
        this.ammunations = ammunations;
    }

    public int getAmmunations() {
        return this.ammunations;
    }

    @Override
    public boolean usable() {
        return this.ammunations > 0;
    }

    @Override
    abstract public void use(Entity e, double angle);

    @Override
    public void addAmmo(int n) {
        if (n > 0) {
            this.ammunations += n;
        }
    }
}