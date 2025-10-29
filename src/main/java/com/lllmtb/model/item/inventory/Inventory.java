package com.lllmtb.model.item.inventory;

import com.lllmtb.model.item.IItems;
import java.util.HashSet;
import java.util.Set;

public class Inventory {
    public final Set<IItems> inventory = new HashSet<IItems>();

    public void shoot(IItems i, Entity e, double angle) {
        i.use(e, angle);
    }
}
