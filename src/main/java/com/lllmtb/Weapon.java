package com.lllmtb;

public abstract class Weapon implements IItems {

    private int ammunitions;
    private Projectile projectileType;
    private boolean unlocked;

    public Weapon (int ammunitions, Projectile type, boolean unlocked) {
        this.ammunitions = ammunitions;
        this.projectileType = type;
        this.unlocked = unlocked;
    }

    @Override
    public boolean inInventory() {
        return this.unlocked;
    }

    @Override
    public boolean useable() {
        return this.inInventory() && this.ammunitions > 0;
    }

    @Override
    public void oneUse() {
        if (this.useable()) {
            this.ammunitions--;
        } else {
            throw new IllegalArgumentException("The weapon can't be used!");
        }
    }

    /*public void shoot(Worm shooter, Worm target) {
    }*/
}
