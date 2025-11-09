package com.lllmtb.model.item.inventory;

import com.lllmtb.model.item.tool.Parachute;
import com.lllmtb.model.item.weapon.Shotgun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;
import com.lllmtb.model.item.tool.Tool;
import com.lllmtb.model.item.weapon.Weapon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryTest {

    public static Set<Weapon> weapons = new HashSet<>();
    public static Map<Class<? extends IItems>, Integer> items = new HashMap<>();
    public static Inventory ivt;
    public static Shotgun sg = new Shotgun();
    public static Parachute p = new Parachute();

    @BeforeEach
    public void steUp() {
        ivt = new Inventory(weapons, items);
    }

    @Test
    public void testConstructeur1() {
        assertEquals(ivt.getAmmos(), items);
    }

    @Test
    public void testConstructeur2() {
        assertEquals(ivt.getWeaponsUnlocked(), weapons);
    }
}
