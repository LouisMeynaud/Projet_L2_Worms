package com.lllmtb.model.item.weapon;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShotgunTest {

    public static Shotgun sg1;
    public static Shotgun sg2;
    public static Shotgun sg3;
    public static Shotgun sg4;

    @BeforeEach
    public static void setUp() {
        sg1 = new Shotgun(10,true);
        sg2 = new Shotgun(10, false);
        sg3 = new Shotgun(0,true);
        sg4 = new Shotgun(0, false);
    }

    @Test
    public void testConstructeursg1Ammunations() {
        assertEquals(10, sg1.getAmmunations());
    }

    @Test
    public void testConstructeursg1Unlocked() {
        assertTrue(sg1.inInventory());
    }

    @Test
    public void testsg1Usable() {
        assertTrue(sg1.usable());
    }

    @Test
    public void testsg2Usable() {
        assertFalse(sg2.usable());
    }

    @Test
    public void testsg3Usable() {
        assertFalse(sg2.usable());
    }

    @Test
    public void testsg4Usable() {
        assertFalse(sg2.usable());
    }

    @Test
    public void testsg1UnlockItem() {
        sg1.unlockItem();
        assertTrue(sg1.getUnlocked());
    }

    @Test
    public void testsg2UnlockItem() {
        sg2.unlockItem();
        assertTrue(sg2.getUnlocked());
    }

    @Test
    public void testsg1AddAmmo0() {
        sg1.addAmmo(0);
        assertEquals(10, sg1.getAmmunations());
    }

    @Test
    public void testsg1AddAmmo10() {
        sg1.addAmmo(10);
        assertEquals(20, sg1.getAmmunations());
    }

    @Test
    public void testsg1AddAmmoMoins10() {
        sg1.addAmmo(-10);
        assertEquals(10, sg1.getAmmunations());
    }
}
