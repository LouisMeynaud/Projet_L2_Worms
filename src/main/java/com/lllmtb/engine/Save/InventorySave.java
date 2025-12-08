package com.lllmtb.engine.Save;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import java.util.Map;
import java.util.Set;

public class InventorySave {
    private Map<ToolId, Integer> toolAmmo;
    private Map<WeaponId, Integer> weaponAmmo;
    private Set<WeaponId> unlockedWeapons;


    public InventorySave(Inventory inventory) {
        this.toolAmmo = inventory.tools.ammoSnapshot();
        this.weaponAmmo =  inventory.weapons.ammoSnapshot();
        this.unlockedWeapons = inventory.unlockedWeapons();
    }

    public InventorySave() {

    }
}
