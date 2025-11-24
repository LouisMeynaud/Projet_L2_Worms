package com.lllmtb.engine;

import com.lllmtb.events.TurnEvents.TurnEndEvent;
import com.lllmtb.events.TurnEvents.TurnStarEvent;
import com.lllmtb.events.TurnEvents.WormPlayable;

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
        GameSettings settings = new GameSettings(2, new String[]{"Team1", "Team2"}, 2, false, null, false);
        this.gw = new GameWorld(settings);
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
    void testStart(){
        AtomicBoolean event1 = new AtomicBoolean(false);
        AtomicBoolean event2 = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnStarEvent.class, event -> {
            event1.set(true);});
        this.gw.getBus().subscribe(WormPlayable.class, event -> {
            event2.set(true);
        });
        gw.getBus().postQueued(new TurnEndEvent());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        assertTrue(event1.get());
        assertTrue(event2.get());
    }


    @Test
    void testUpdateEndWithTime(){
        AtomicBoolean eventEnd = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnEndEvent.class, event -> {
            eventEnd.set(true);});
        try {
            Thread.sleep(2*gw.getTourDuration());
        } catch (InterruptedException ignored) {}
        assertTrue(eventEnd.get());

    }

    @Test
    void testUpdateEndWithEvent(){
        AtomicBoolean eventEnd = new AtomicBoolean(false);
        this.gw.getBus().subscribe(TurnEndEvent.class, event -> {
            eventEnd.set(true);});
        gw.getBus().postQueued(new TurnEndEvent());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        assertTrue(eventEnd.get());

    }
}
