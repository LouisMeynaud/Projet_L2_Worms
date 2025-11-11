package com.lllmtb.model.item.weapon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponRegistryTest {

    @Test
    public void testException() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new GunWeapon());
        weapons.add(new GunWeapon());
        try {
            new WeaponRegistry(weapons);
        } catch (IllegalArgumentException e) {
            assertEquals("Duplicate weapon id: " + WeaponId.GUN, e.getMessage());
        }
    }

    @Test
    public void testAll() {
        List<Weapon> weapons = new ArrayList<>();
        GunWeapon gun = new GunWeapon();
        Bazooka b = new Bazooka();
        weapons.add(gun);
        weapons.add(b);
        WeaponRegistry wr = new WeaponRegistry(weapons);
        Map<WeaponId, Weapon> m = new HashMap<>();
        m.put(gun.id(), gun);
        m.put(b.id(), b);
        assertTrue(wr.all().containsAll(m.values()));
    }
 }
