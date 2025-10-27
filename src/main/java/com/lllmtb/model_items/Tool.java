package com.lllmtb.model_items;

public abstract class Tool implements IItems {

    private int ammunations;
    private boolean unlocked;

    public Tool(int ammunations, boolean unlocked) {
        this.ammunations = ammunations;
        this.unlocked = unlocked;
    }

    @Override
    public boolean inInventory() {
        return this.unlocked;
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
            throw new IllegalArgumentException("The tool can't be used!");
        }
    }
}
