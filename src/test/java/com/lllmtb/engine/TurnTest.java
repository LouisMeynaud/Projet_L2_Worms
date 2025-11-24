package com.lllmtb.engine;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TurnTest {

    private Turn turn;
    private Team team = new Team("TestTeam", 1, new Position[]{new Position(0, 0)});

    @BeforeEach
    public void setUp(){
        this.turn = new Turn(team);
    }

    @Test
    public void testConstructor(){
        assertSame(this.turn.getTeam(), this.team);
    }

    @Test
    public void testGetCurrentWorm(){
        assertSame(this.turn.getWormPlayable(), team.getCurrentWorm());
    }

    @Test
    public void testEnd(){
        Team team2 = new Team("TestTeam", 2, new Position[]{new Position(0, 0)});
        Turn turn2 = new Turn(team2);
        Worm worm1 = turn2.getWormPlayable();
        turn2.end();
        assertNotSame(turn2.getWormPlayable(), worm1);
    }

    @Test
    public void testGetIsEndFalse(){
        assertFalse(turn.getIsEnd());
    }

    @Test
    public void testGetIsEndTrue(){
        this.turn.setIsEnd();
        assertTrue(turn.getIsEnd());
    }



}
