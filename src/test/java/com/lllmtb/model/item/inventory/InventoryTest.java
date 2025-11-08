package com.lllmtb.model.item.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    public static Inventory i;

    @BeforeEach
    public void setUp() {
        i = new Inventory(1);
    }

    @Test
    public void testConstructeur() {
        assertEquals(1, i.getTest());
    }
}
