package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.TurnEvents.TurnEndEvent;
import com.lllmtb.events.TurnEvents.TurnStarEvent;
import com.lllmtb.events.TurnEvents.TurnStopEvent;
import com.lllmtb.events.TurnEvents.WormPlayable;

import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static  org.junit.jupiter.api.Assertions.*;

public class TurnManagerTest {
    private TurnManager turnManager;
    private GameWorld gw;

    @BeforeEach
    public void setUp(){
        GameSettings settings = new GameSettings(2, new String[]{"TeamTest1", "TeamTest2"}, 1, false, null, false,370, LandScape.HILLY);
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
        this.gw.getBus().subscribe(TurnStarEvent.class, event -> {
            event1.set(true);});
        gw.getBus().postQueued(new TurnStopEvent());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        assertTrue(event1.get());
    }

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
            Thread.sleep(gw.getTurnDuration() + 50);    // let the event bus dispatch the event
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
}
