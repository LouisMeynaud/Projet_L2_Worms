package com.lllmtb.model.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.item.weapon.WeaponId;

class ShopElementTest {

    @Test
    void constructor_assignsValuesCorrectly() {
        ShopElement element = new ShopElement(WeaponId.GUN, "TestGun", 100, 50, 10);

        assertEquals(WeaponId.GUN, element.id);
        assertEquals("TestGun", element.name);
        assertEquals(100, element.unlockPrice);
        assertEquals(50, element.ammoPrice);
        assertEquals(10, element.ammoPackSize);
    }
}
