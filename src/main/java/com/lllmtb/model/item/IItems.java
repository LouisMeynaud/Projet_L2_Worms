package com.lllmtb.model.item;

public interface IItems {

    boolean inInventory();

    boolean useable();

    void use(Entity e, double angle);
}