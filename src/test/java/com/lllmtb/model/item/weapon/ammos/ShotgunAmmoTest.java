package com.lllmtb.model.item.weapon.ammos;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.weapon.Ammos.ShotgunAmmo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShotgunAmmoTest {

    public static ShotgunAmmo s;

    @BeforeEach
    public void setUp() {
        s = new ShotgunAmmo(10);
    }

    @Test
    public void testGetter() {
        assertEquals(10, s.getNumberOfAmmos());
    }

    @Test
    public void testAddAmmos() {
        s.addAmmos(10);
        assertEquals(20, s.getNumberOfAmmos());
    }

    @Test
    public void testShoot() {
        Entity mockE = mock(Entity.class);
        double angle = 10;
        s.shoot(mockE, angle);
        assertEquals(9, s.getNumberOfAmmos());
    }
}
