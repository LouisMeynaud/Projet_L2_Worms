package com.lllmtb.engine.turn;

import java.util.concurrent.atomic.AtomicBoolean;

import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;

public class TurnManagerTest {
    private TurnManager turnManager;
    private GameWorld gw;
    private EventBus eb;

    @BeforeEach
    public void setUp(){
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"TeamTest1", "TeamTest2"}, 1, 100, 10, false, null, false, 370, LandScape.HILLY, 2,GameMode.NORMAL);
        this.eb = new EventBus();
        this.gw = new GameWorld(settings, eb);
        gw.start();

        this.turnManager = gw.getTurnManager();
    }

    @AfterEach
    public void endSetUp(){
        gw.stop();
    }

    @Test
    void testConstructor(){
        assertNotNull(turnManager.getWorld());
        assertEquals(10000, this.turnManager.getTurnDuration());
        assertEquals(9, this.eb.listenerCount());
    }

    @Test
    void testGetWorld(){
        assertSame(gw, turnManager.getWorld());
    }

    @Test
    void testGetTurn(){
        Team t = new Team("Test", 1, new Position[]{new Position(1, 2)});
        turnManager.startTurn(t);
        assertNotNull(turnManager.getTurn());
        assertSame(t, turnManager.getTurn().getTeam());
    }

    @Test
    void testGetNbTurn(){
        Team t = new Team("Test", 1, new Position[]{new Position(1, 2)});
        turnManager.endTurn();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        assertEquals(2, this.turnManager.getNbTurn());
    }

    @Test
    void testSpawnCoins(){
        //First Turn already lauch
        boolean spawnCoin = false;
        for(PhysicsBody body : this.gw.getPhysics().getBodies()){
            if(body.getEntity() instanceof Coin){
                spawnCoin = true;
            }
        }
        assertTrue(spawnCoin);
    }

    @Test
    void cleanTurnProjectile(){
        Worm worm = new Worm("Test", new Team("T", 1, new Position[]{new Position(1,1)}));
        new BulletProjectile(new UseParams(worm));
        gw.getBus().postQueued(new TurnStopEvent());
        assertEquals(1,turnManager.getNbTurn());
        try {
            Thread.sleep( 1000);
        } catch (InterruptedException ignored) {}
        assertEquals(2, turnManager.getNbTurn());
    }


    @Test
    void testStartTurnStarEvent(){
        AtomicBoolean event1 = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnStartEvent.class, event -> {
            event1.set(true);});
        gw.getBus().postQueued(new TurnStopEvent());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        assertTrue(event1.get());
    }

    @Test
    void testStartWormPlayable(){
        AtomicBoolean event2 = new AtomicBoolean(false);
        this.gw.getBus().subscribe(WormPlayable.class, event -> {
            event2.set(true);
        });
        gw.getBus().postQueued(new TurnStopEvent());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        assertTrue(event2.get());

    }


    @Test
    void testUpdateEndWithTime(){
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"TeamTest1", "TeamTest2"}, 1, 100, 1, false, null, false, 370, LandScape.HILLY, 2,GameMode.NORMAL);
        EventBus specialEB = new EventBus();;
        GameWorld specialGW = new GameWorld(settings, specialEB);
        specialGW.start();
        TurnManager specialTM = specialGW.getTurnManager();

        AtomicBoolean eventEnd = new AtomicBoolean(false);
        specialGW.getBus().subscribe(TurnEndEvent.class, event -> eventEnd.set(true));
        try {
            Thread.sleep(specialGW.getTurnDuration() + 200);    // let the event bus dispatch the event
        } catch (InterruptedException ignored) {}
        assertTrue(eventEnd.get());

    }

    @Test
    void testUpdateEndWithEvent(){
        AtomicBoolean eventEnd = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnEndEvent.class, event -> {
            eventEnd.set(true);});
        gw.getBus().postQueued(new TurnStopEvent());
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        assertTrue(eventEnd.get());
    }

    @Test
    void testEndWithOneTeamAlive(){
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"TeamTest1", "TeamTest2"}, 1, 100, 1, false, null, false, 370, LandScape.HILLY, 2, GameMode.NORMAL);
        EventBus eventBus = new EventBus();
        GameWorld gw2 = new GameWorld(settings, eventBus);
        gw2.start();
        int lastIndex = gw2.getIndexTeam();
        gw2.getBus().postQueued(new TurnStopEvent());
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
        assertEquals(lastIndex, gw2.getIndexTeam());
    }

    @Test
    void testEndWithMaxShootReached(){
        AtomicBoolean eventEnd = new AtomicBoolean(false);
        TurnManager.setRemainingTimeAfterShootInMs(10);
        this.gw.getBus().subscribe(TurnEndEvent.class, event -> {
            eventEnd.set(true);});
        Team team1 = new Team("TestTeam", 1, new Position[]{new Position(1,1)});
        turnManager.startTurn(team1);
        team1.unlockWeapon(WeaponId.GUN);
        team1.addAmmo(WeaponId.GUN, 10);
        team1.selectWeapon(WeaponId.GUN);
        UseParams params = new UseParams(0, new Position(0, 0), team1.getCurrentWorm());
        for(int i = 0; i < Turn.limitshoot; i++){
            team1.use(params);
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
        assertTrue(eventEnd.get());
    }
}
