package com.lllmtb.model.entity.team;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TeamTest {
    public Team team;

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
        assertNotEquals(null, team.getWorms());
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
    public void testSetUpPlayers(){
        assertEquals(1, team.getWorms().size());
        Worm worm1 = team.getWorms().values().iterator().next();
        assertEquals("TestTeam1", worm1.name);
        assertEquals(team, worm1.getTeam());

    }
}
