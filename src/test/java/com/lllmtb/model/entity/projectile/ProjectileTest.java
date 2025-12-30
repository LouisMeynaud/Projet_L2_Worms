package com.lllmtb.model.entity.projectile;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;

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
    void getCollisionRocket(){
        RocketProjectile rp = new RocketProjectile(params);
        assertEquals(CollisionType.TOUCH, rp.getCollisionType());

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
        Team t = Mockito.mock(Team.class);
        Worm worm = new Worm("Test", t);

        BulletProjectile bullet = new BulletProjectile(params);
        List<GameCommand> lbullet = bullet.onImpact(worm);
        assertFalse(lbullet.isEmpty());

        RocketProjectile rocket = new RocketProjectile(params);
        List<GameCommand> lrocket = rocket.onImpact(worm);
        assertFalse(lrocket.isEmpty());

        MissileProjectile missile = new MissileProjectile(params);
        List<GameCommand> lmissile = missile.onImpact(worm);
        assertFalse(lmissile.isEmpty());
    }

    @Test
    public void onImpact_whenWormDies_addsRemoveWormCommand() {
        RocketProjectile rocket = new RocketProjectile(params);

        Worm deadWorm = Mockito.mock(Worm.class);
        Mockito.when(deadWorm.isAlive()).thenReturn(false);
        List<GameCommand> commands = rocket.onImpact(deadWorm);
        assertEquals(4, commands.size());
    }

    @Test
    public void onimpact_whenFriendlyFireEnabled(){
        BulletProjectile bullet = new BulletProjectile(params);
        Projectile.setFriendlyFire(true);
        Worm wormHit = new Worm("Test",this.owner.getTeam());
        double hp = wormHit.getStats().getHp();
        bullet.onImpact(wormHit);
        assertNotEquals(wormHit.getStats().getHp(), hp);
    }

    @Test
    public void onimpact_whenFriendlyFireDisable(){
        BulletProjectile bullet = new BulletProjectile(params);
        Projectile.setFriendlyFire(false);
        Worm wormHit = new Worm("Test",this.owner.getTeam());
        double hp = wormHit.getStats().getHp();
        bullet.onImpact(wormHit);
        assertEquals(wormHit.getStats().getHp(), hp);
    }
}