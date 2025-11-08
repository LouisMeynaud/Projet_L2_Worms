package com.lllmtb.model.item.weapon.Ammos;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.weapon.Shotgun;

public class ShotgunAmmo extends Ammos {

    public ShotgunAmmo(int n) {
        super(n);
    }

    @Override
    public void shoot(Entity e, double angle) {
        this.addAmmos(-1);
        //appel dans projectile pour simuler le tir
    }
}
