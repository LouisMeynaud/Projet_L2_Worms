package com.lllmtb.engine.turn;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
    public void setUp(){
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"TeamTest1", "TeamTest2"}, 1, 100, 1, false, null, false, 370, LandScape.HILLY, 2,GameMode.NORMAL);
        EventBus eventBus = new EventBus();
        this.gw = new GameWorld(settings, eventBus);
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
        AtomicBoolean eventEnd = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnEndEvent.class, event -> eventEnd.set(true));
        try {
            Thread.sleep(gw.getTurnDuration() + 100);    // let the event bus dispatch the event
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
}
