package com.lllmtb.model.item.inventory;

import com.lllmtb.model.item.tool.Parachute;
import com.lllmtb.model.item.weapon.Shotgun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.lllmtb.model.item.Item;
import com.lllmtb.model.item.weapon.Weapon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryTest {

    public static Set<Weapon> weaponsEmpty = new HashSet<>();
    public static Set<Weapon> weaponsNoEmpty = new HashSet<>();
    public static Map<Class<? extends Item>, Integer> itemsEmpty = new HashMap<>();
    public static Map<Class<? extends Item>, Integer> itemsNoEmpty = new HashMap<>();
    public static Inventory ivt1;
    public static Inventory ivt2;
    public static Shotgun sg = new Shotgun();
    public static Parachute p = new Parachute();

    @BeforeEach
    public void setUp() {
        weaponsNoEmpty.clear();
        weaponsEmpty.clear();
        itemsEmpty.clear();
        itemsNoEmpty.clear();
        weaponsNoEmpty.add(sg);
        itemsNoEmpty.put(sg.getClass(), 0);
        itemsNoEmpty.put(p.getClass(), 0);
        ivt1 = new Inventory(weaponsEmpty, itemsEmpty);
        ivt2 = new Inventory(weaponsNoEmpty, itemsNoEmpty);
    }

    @Test
    public void testConstructeur1() {
        assertEquals(ivt1.getAmmos(), itemsEmpty);
    }

    @Test
    public void testConstructeur2() {
        assertEquals(ivt1.getWeaponsUnlocked(), weaponsEmpty);
    }

    @Test
    public void testAddAmmo1() {
        ivt1.addAmmo(p, 10);
        itemsEmpty.put(p.getClass(), 10);
        assertEquals(itemsEmpty, ivt1.getAmmos());
    }

    @Test
    public void testAddAmmo2() {
        ivt1.addAmmo(sg, 10);
        itemsEmpty.put(sg.getClass(), 10);
        assertEquals(itemsEmpty, ivt1.getAmmos());
    }

    @Test
    public void testAddAmmo3() {
        ivt2.addAmmo(sg, 10);
        assertEquals(10, ivt2.getAmmos().get(sg.getClass()));
    }

    @Test
    public void testAddItemTool1() {
        ivt1.addItem(p, 0);
        itemsEmpty.put(p.getClass(), 0);
        assertEquals(itemsEmpty, ivt1.getAmmos());
    }

    @Test
    public void testAddItemTool2() {
        ivt2.addItem(p, 5);
        assertEquals(5, ivt2.getAmmos().get(p.getClass()));
    }

    @Test
    public void testAddItemWeapon1() {
        ivt1.addItem(sg, 10);
        itemsEmpty.put(sg.getClass(), 10);
        assertEquals(itemsEmpty, ivt1.getAmmos());
    }

    @Test
    public void testAddItemWeapon2() {
        ivt1.addItem(sg,10);
        weaponsEmpty.add(sg);
        assertEquals(weaponsEmpty, ivt1.getWeaponsUnlocked());
    }

    @Test
    public void testAddItemWeapon3() {
        ivt2.addItem(sg, 5);
        assertEquals(5, ivt2.getAmmos().get(sg.getClass()));
    }

    @Test
    public void testUseableWeapon1() {
        assertFalse(ivt1.useable(sg));
    }

    @Test
    public void testUseableWeapon2() {
        assertFalse(ivt2.useable(sg));
    }

    @Test
    public void testUseableWeapon3() {
        ivt2.addItem(sg, 10);
        assertTrue(ivt2.useable(sg));
    }

    @Test
    public void testUseableTool1() {
        assertFalse(ivt1.useable(p));
    }

    @Test
    public void testUseableTool2() {
        assertFalse(ivt2.useable(p));
    }

    @Test
    public void testUseableTool3() {
        ivt2.addItem(p, 10);
        assertTrue(ivt2.useable(p));
    }
}
