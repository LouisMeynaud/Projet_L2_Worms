package com.lllmtb.model.item;

public interface IItems {

    boolean inInventory();

    boolean usable();

    void use(Entity e, double angle);

    void unlockItem();

    void addAmmo(int n);
}