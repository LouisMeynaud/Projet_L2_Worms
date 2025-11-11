package com.lllmtb.model.entity.worm;

import com.lllmtb.model.item.weapon.WeaponId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Position;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class WormTest {
    public Worm worm;
    public Team team;
    

    @BeforeEach
    public void setUp(){
        String name = "Test";
        Position pos = new Position(0, 0);
        team =  Mockito.mock(Team.class);
        worm = new Worm(name, team, pos);
    }


    @Test
    public void testConstructorName(){
        assertEquals("Test", worm.name);
    }

    @Test
    public void testConstructorStats(){
        assertNotEquals(null, worm.getStats());
    }

    @Test
    public void testConstructorTeam(){
        assertEquals(team, worm.getTeam());
    }

    @Test
    public void testSetItem(){
        WeaponId gun = WeaponId.GUN;
        assertNull(worm.getSelfItem());
        worm.setSelfItem(gun);
        assertEquals(gun, worm.getSelfItem());
    }

    @Test
    public void testIsAliveTrue(){
        assertTrue(worm.isAlive());
    }
    @Test
    public void testIsAliveFalse(){
        worm.setDeath();
        assertFalse(worm.isAlive());
    }
    @Test
    public void testSpawn(){
        Position pos = new Position(1,1);
        worm.spawn(pos);
        assertEquals(pos, worm.getBody().getPos());
    }


}
