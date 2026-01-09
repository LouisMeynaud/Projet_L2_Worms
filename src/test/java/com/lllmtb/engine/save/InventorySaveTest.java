package com.lllmtb.engine.save;

import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.weapon.WeaponId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InventorySaveTest {

    @Test
    public void constructorTest() {
        Inventory inventory = new Inventory();
        InventorySave inventorySave = new InventorySave(inventory);
        assertNotNull(inventorySave);
        assertEquals(inventorySave.toolAmmo, inventory.tools.ammoSnapshot());
        assertEquals(inventorySave.weaponAmmo, inventory.weapons.ammoSnapshot());
    }
}
