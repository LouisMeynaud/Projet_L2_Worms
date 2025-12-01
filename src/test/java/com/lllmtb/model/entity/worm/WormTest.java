package com.lllmtb.model.entity.worm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Position;

import static org.junit.jupiter.api.Assertions.*;

public class WormTest {
    public Worm worm;
    public Team team;
    

    @BeforeEach
    public void setUp(){
        String name = "Test";
        Position pos = new Position(0, 0);
        team = new Team("Team", 1, new Position[]{pos});
        worm = new Worm(name, team);
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
    public void testIsAliveTrue(){
        assertTrue(worm.isAlive());
    }
    @Test
    public void testIsAliveFalse(){
        worm.getStats().setHp(0);
        worm.setDeath();
        assertFalse(worm.isAlive());
    }
    @Test
    public void testSetDeathFailed(){
        worm.setDeath();
        assertTrue(worm.isAlive());
    }
    @Test
    public void testSpawn(){
        Position pos = new Position(1,1);
        worm.spawn(pos);
        assertEquals(pos, worm.getBody().getPos());
    }

    @Test
    public void testToString() {
        assertDoesNotThrow(() -> worm.toString());
        assertTrue(worm.isAlive());
        assertTrue(worm.toString().contains("♥"));
        worm.getStats().setHp(0);
        worm.setDeath();
        assertFalse(worm.isAlive());
        assertTrue(worm.toString().contains("☠"));
    }

}
