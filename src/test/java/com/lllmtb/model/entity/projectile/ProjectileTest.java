package com.lllmtb.model.entity.projectile;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {
    private Worm owner;
    private Position spawn;
    private UseParams params;

    @BeforeEach
    public void setUp() {
        Team team = new Team("TestTeam", 1, new Position[]{new Position(0, 0)});
        this.owner = new Worm("Test", team);
        this.spawn = new Position(3.0, 0.0);
        double angle = Math.PI / 4.0;
        this.params = new UseParams(angle, spawn, owner);
    }

    @Test
    public void constructorSetsOwnerAndBody_notNull() {
        BulletProjectile bullet = new BulletProjectile(params);
        assertSame(owner, bullet.getOwner(), "Projectile.getOwner() should return the owner passed to the constructor");
        assertNotNull(bullet.getBody(), "Projectile body should not be null after construction");
    }

    @Test
    public void forceField_isSubclassSpecific() throws Exception {
        BulletProjectile bullet = new BulletProjectile(params);

        // read the private 'force' field
        Field subForceField = BulletProjectile.class.getDeclaredField("force");
        subForceField.setAccessible(true);
        double subForceValue = subForceField.getDouble(bullet);

        assertEquals(100.0, subForceValue, 1e-12,
                "Subclass field 'force' should be 60.0 as declared in BulletProjectile.");
    }

    @Test
    public void onImpact_doesNotThrow() {
        // TODO
        BulletProjectile bullet = new BulletProjectile(params);
        assertDoesNotThrow(bullet::onImpact);

        RocketProjectile rocket = new RocketProjectile(params);
        assertDoesNotThrow(rocket::onImpact);
    }
}