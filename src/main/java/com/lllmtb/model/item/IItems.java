package com.lllmtb.model.item;

import com.lllmtb.model.entity.Entity;

public interface IItems {

    boolean usable();

    void use(Entity e, double angle);

    void addAmmo(int n);
}