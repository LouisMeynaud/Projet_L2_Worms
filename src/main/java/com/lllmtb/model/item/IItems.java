package com.lllmtb.model.item;

import com.lllmtb.model.entity.Entity;

public interface IItems {

    int getAmmunations();

    boolean getUnlocked();

    boolean inInventory();

    boolean usable();

    void use(Entity e, double angle);

    void unlockItem();

    void addAmmo(int n);
}