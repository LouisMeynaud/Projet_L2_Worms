package com.lllmtb.model.item.weapon.Ammos;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.weapon.Shotgun;
import com.lllmtb.model.item.weapon.Weapon;

public abstract class Ammos {

    private int numberOfAmmos;

    public Ammos(int numberOfAmmos) {
        this.numberOfAmmos = numberOfAmmos;
    }

    public int getNumberOfAmmos() {
        return numberOfAmmos;
    }

    public void addAmmos(int n) {
        this.numberOfAmmos += n;
    }

    public abstract void shoot(Entity e, double angle);
}
