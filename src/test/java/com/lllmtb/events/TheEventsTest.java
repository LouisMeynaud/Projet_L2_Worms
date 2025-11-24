package com.lllmtb.events;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.graphicsUi.*;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;


public class TheEventsTest {

  

    @Test
    public void GamePauseEventTest() {
        
        GamePauseEvent eventTrue = new GamePauseEvent(true);
        assertTrue(eventTrue.isPaused, "devrait stocker true");
        GamePauseEvent eventFalse = new GamePauseEvent(false);
        assertFalse(eventFalse.isPaused, "devrait stocker false");
    }

    @Test
    public void StartGameEventTest() {
        GameSettings mockSettings = mock(GameSettings.class);
        StartGameEvent event = new StartGameEvent(mockSettings);
        assertNotNull(event);
        assertSame(mockSettings, event.getSettings());
    }

    @Test
    public void WorldCreatedEventTest() {
        GameWorld mockWorld = mock(GameWorld.class);
        WorldCreatedEvent event = new WorldCreatedEvent(mockWorld);
        assertNotNull(event);
        assertSame(mockWorld, event.getWorld());
    }

   
    @Test
    public void eventWithoutArgumentTest() {
        eventWithoutArgument(new GameStateUpdatedEvent());
        eventWithoutArgument(new ScreenGenerateMap());
        eventWithoutArgument(new ScreenHomeEvent());
        eventWithoutArgument(new ScreenLoadMap());
        eventWithoutArgument(new ScreenNewOrLoadEvent());
        eventWithoutArgument(new ScreenSettingsGame());
    }

  
    private void eventWithoutArgument(Event event) {
        assertNotNull(event);
        assertTrue(event instanceof Event);
    }
}