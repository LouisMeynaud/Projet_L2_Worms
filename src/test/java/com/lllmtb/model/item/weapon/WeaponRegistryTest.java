package com.lllmtb.model.item.weapon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class WeaponRegistryTest {

    @Test
    public void testException() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new GunWeapon());
        weapons.add(new GunWeapon());
        try {
            new WeaponRegistry();
        } catch (IllegalArgumentException e) {
            assertEquals("Duplicate weapon id: " + WeaponId.GUN, e.getMessage());
        }
    }
 }
