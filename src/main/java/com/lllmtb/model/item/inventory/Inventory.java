package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;
import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private final Set<IItems> inventory = new HashSet<IItems>();

    public void shoot(IItems i, Entity e, double angle) {
        if (i.usable()) {
            i.use(e, angle);
        }
    }

    public IItems selectItems(int id) throws Unusable {
        IItems[] itemsList = (IItems[]) this.inventory.toArray();
        IItems item = itemsList[id];
        if (item.usable()) {
            return item;
        }
        throw new Unusable();
    }

    public void addAmmo(IItems item, int n) {
        if (this.inventory.contains(item)) {
            item.addAmmo(n);
        }
    }
}
