package com.lllmtb.engine.turn;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TurnTest {

    private Turn turn;
    private Team team = new Team("TestTeam", 1, new Position[]{new Position(0, 0)},0);

    @BeforeEach
    public void setUp(){
        this.turn = new Turn(team);
    }

    @Test
    public void testConstructorTeam(){
        assertSame(this.turn.getTeam(), this.team);
    }

    @Test
    public void testContructorCurrentWorm(){
        Team team2 = new Team("TestTeam", 2, new Position[]{new Position(0, 0), new Position(0,0)},0);
        Worm worm1 = team2.getCurrentWorm();
        worm1.damageWorm(100);
        Turn turn2 = new Turn(team2);
        assertNotSame(turn2.getWormPlayable(), worm1);
    }

    @Test
    public void testGetCurrentWorm(){
        assertSame(this.turn.getWormPlayable(), team.getCurrentWorm());
    }

    @Test
    public void testEnd(){
        Team team2 = new Team("TestTeam", 2, new Position[]{new Position(0, 0), new Position(0,0)},0);
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

    @Test
    public void testMaxShootNotReached(){
        Team team1 = new Team("TestTeam", 1, new Position[]{new Position(1,1)}, 0);
        Turn turn1 = new Turn(team1);
        team1.unlockWeapon(WeaponId.GUN);
        team1.addAmmo(WeaponId.GUN, 10);
        team1.selectWeapon(WeaponId.GUN);

        UseParams params = new UseParams(team1.getCurrentWorm());
        team1.use(params);
        assertFalse(turn1.teamMaximumShoot());
    }

    @Test
    public void testMaxShootReached(){
        Team team1 = new Team("TestTeam", 1, new Position[]{new Position(1,1)}, 0);
        Turn turn1 = new Turn(team1);
        team1.unlockWeapon(WeaponId.GUN);
        team1.addAmmo(WeaponId.GUN, 10);
        team1.selectWeapon(WeaponId.GUN);
        UseParams params = new UseParams(team1.getCurrentWorm());
        for(int i = 0; i < Turn.limitshoot; i++){
            team1.use(params);
        }

        assertTrue(turn1.teamMaximumShoot());

    }

    @Test
    public void testNextWormPlayable(){
        Team team = new Team("TestTeam", 1, new Position[]{new Position(0, 0)},0) ;
        Worm worm1 = team.getIndexWorm(0);
        Worm worm2 = new Worm("Worm2", team);
        Turn turn = new Turn(team);
        Worm lastCurrentWorm = turn.getWormPlayable();
        turn.nextWormPlayable();
        //Different Worm if two worm is alive
        assertNotEquals(turn.getWormPlayable(), lastCurrentWorm);

        lastCurrentWorm = turn.getWormPlayable();
        for(Worm worm : team.getTeamWorms()){
            if (worm != turn.getWormPlayable()){
               worm.damageWorm(1000);
            }
        }
        turn.nextWormPlayable();
        //Same worm if only one worm is alive
        assertEquals(lastCurrentWorm, turn.getWormPlayable());
    }



}
