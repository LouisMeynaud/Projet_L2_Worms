package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.Entity;

public class Shotgun extends Weapon {

    public Shotgun(int ammunations, boolean unlocked) {
        super(ammunations, unlocked);
    }

    @Override
    public void use(Entity e, double angle) {
        //on simule une utilisation du shotgun
    }
}
