package com.lllmtb.model.entity.worm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public class WormTest {
    public Worm worm; 
    public Team team;


    @BeforeEach
    public void setUp(){
        String name = "Test";
        Position pos = new Position(0, 0);
        team = new Team("Team", 1, new Position[]{pos}, 0);
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
        worm.damageWorm(worm.getStats().getHp());
        assertFalse(worm.isAlive());
    }
    @Test
    public void testSetDeathFailed(){
        worm.damageWorm(0);
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
        worm.damageWorm(worm.getStats().getHp());
        assertFalse(worm.isAlive());
        assertTrue(worm.toString().contains("☠"));
    }

    //TODO : refaire se test
    // @Test
    // public void testGetAnimation() {
        
    //     assertNotNull(worm.getAnimation());
    // }

    @Test
    public void testGetFacingDirection() {
        worm.setFacingDirection(1);
        assertEquals(1, worm.getFacingDirection());

        worm.setFacingDirection(-1);
        assertEquals(-1, worm.getFacingDirection());
    }

    @Test
    public void testSetFacingDirection() {
        worm.setFacingDirection(1);
        assertEquals(1, worm.getFacingDirection());

        worm.setFacingDirection(-1);
        assertEquals(-1, worm.getFacingDirection());

        worm.setFacingDirection(0);
        assertEquals(-1, worm.getFacingDirection());
    }

    @Test
    public void testUpdateFacingDirection() {
        worm.getBody().setVector(new Vector(1, 0));
        worm.updateFacingDirection();
        assertEquals(1, worm.getFacingDirection());

        worm.getBody().setVector(new Vector(-1, 0));
        worm.updateFacingDirection();
        assertEquals(-1, worm.getFacingDirection());

        worm.getBody().setVector(new Vector(0, 0));
        worm.updateFacingDirection();
        assertEquals(-1, worm.getFacingDirection());
    }
}
