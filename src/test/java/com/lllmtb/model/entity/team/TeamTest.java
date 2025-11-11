package com.lllmtb.model.entity.team;

import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    public Team team;
    public WeaponId gun = WeaponId.GUN;

    @BeforeEach
    public void setUp(){
        team = new Team("TestTeam", 1, new Position(0,0));


    }

    @Test
    public void testConstructorInventory(){
        assertNotEquals(null, team.getInventory());
    }
    @Test
    public void testConstructorWorms(){
        Worm worm = team.getTeamWorms().get(0);
        assertNotNull(worm);
        assertEquals("TestTeam1",worm.name);
    }
    @Test
    public void testConstructorName(){
        assertEquals("TestTeam", team.name);
    }
    @Test
    public void testConstructorId(){
        assertNotEquals(0, team.id);
    }
    @Test
    public void testConstructorNumberOfPlayers(){
        assertEquals(1, team.getNumberOfPlayers());
    }
    @Test
    public void testCurrentWorm(){
        Worm worm = team.getIndexWorm(0);
        assertEquals(team.getCurrentWorm(), worm);
    }
    @Test
    public void testSetUpPlayers(){
        assertEquals(1, team.getTeamWorms().size());
        Worm worm1 = team.getIndexWorm(0);
        assertEquals("TestTeam1", worm1.name);
        assertSame(team, worm1.getTeam());
        assertNotNull(worm1.getBody().getPos());
    }
    @Test
    public void testAddPlayer(){
        Worm worm = new Worm("Worm1", team, new Position(0,0));
        team.addPlayer(worm);
        assertEquals(2, team.getNumberOfPlayers() );
        Worm worm2 = team.getIndexWorm(1);
        assertEquals(worm2, worm);
    }
    @Test
    public void testRemovePlayer(){
        team.removePlayer(0);
        assertEquals(0, team.getNumberOfPlayers());
        assertTrue(team.getTeamWorms().isEmpty());
    }
    @Test
    public void testUnlockWeapon(){
        team.unlockWeapon(gun);
        assertTrue(team.getInventory().isUnlocked(gun));
    }
    @Test
    public void testLockWeapon(){
        team.lockWeapon(gun);
        assertFalse(team.getInventory().isUnlocked(gun));
    }
    @Test
    public void testAddAmmo(){
        team.addAmmo(gun, 10);
        assertEquals(10, team.getInventory().getAmmo(gun));
    }
    @Test
    public void testSelectItems(){
        team.unlockWeapon(gun);
        team.selectItem(gun);
        assertEquals(gun, team.getCurrentWorm().getSelfItem());
    }
    @Test
    public void testFire(){
        team.addAmmo(gun, 10);
        team.unlockWeapon(gun);
        team.selectItem(gun);
        double angle = 10;
        Worm worm = team.getCurrentWorm();
        assertEquals(BulletProjectile.class, team.fire(new FireParams(angle, worm.getBody().getPos(), worm)).getClass());
    }
    @Test
    public void testNextWorm(){
        Worm worm = new Worm("Test2", team, new Position(0, 0));
        team.addPlayer(worm);
        team.nextWorm();
        assertSame(worm, team.getCurrentWorm());
    }
    @Test
    public void testNextWormLast(){
        Worm worm = new Worm("Test2", team, new Position(0, 0));
        team.addPlayer(worm);
        team.nextWorm();
        team.nextWorm();
        assertSame(team.getIndexWorm(0), team.getCurrentWorm());

    }



}
