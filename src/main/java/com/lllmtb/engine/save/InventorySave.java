package com.lllmtb.engine.save;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import java.util.Map;
import java.util.Set;

public class InventorySave {
    public Map<ToolId, Integer> toolAmmo;
    public Map<WeaponId, Integer> weaponAmmo;
    public Set<WeaponId> unlockedWeapons;


    public InventorySave(Inventory inventory) {
        this.toolAmmo = inventory.tools.ammoSnapshot();
        this.weaponAmmo =  inventory.weapons.ammoSnapshot();
        this.unlockedWeapons = inventory.unlockedWeapons();
    }

    public InventorySave() {

    }
}
