package com.lllmtb.model.item.weapon;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;
import com.lllmtb.model.item.weapon.Ammos.Ammos;

public abstract class Weapon implements IItems {

    private final Ammos ammosType;

    public Weapon(Ammos ammosType) {
        this.ammosType = ammosType;
    }

    public Ammos getAmmosType() {
        return ammosType;
    }

    @Override
    public boolean usable() {
        return this.ammosType.getNumberOfAmmos() > 0;
    }

    @Override
    public void use(Entity e, double angle) {
        this.ammosType.shoot(e, angle);
    }

    @Override
    public void addAmmo(int n) {
        if (n > 0) {
            this.ammosType.addAmmos(n);
        }
    }
}