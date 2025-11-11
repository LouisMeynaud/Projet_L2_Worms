package com.lllmtb.model.item.weapon;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class BazookaTest {

    @Test
    public void testCreateProjectile() {
        FireParams fp = Mockito.mock(FireParams.class);
        Bazooka b = new Bazooka();
        assertEquals(BulletProjectile.class, (b.createProjectile(fp)).getClass());
    }
}
