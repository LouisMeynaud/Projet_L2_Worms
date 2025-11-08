package com.lllmtb.model.item.weapon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShotgunTest {

    public static Shotgun sg;

    @BeforeEach
    public void setUp() {
        sg = new Shotgun();
    }

    @Test
    public void testConstructeur() {
        assertEquals(1, sg.getProjectileId());
    }
}
