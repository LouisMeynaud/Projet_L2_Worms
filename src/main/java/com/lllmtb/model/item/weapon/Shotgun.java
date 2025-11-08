package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.weapon.Ammos.Ammos;

public class Shotgun extends Weapon {

    public Shotgun(Ammos ammos) {
        super(ammos);
    }

    @Override
    public void use(Entity e, double angle) {
        //on simule une utilisation du shotgun
    }
}
