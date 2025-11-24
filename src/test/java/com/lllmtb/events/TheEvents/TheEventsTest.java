package com.lllmtb.events.TheEvents;

// Imports standard JUnit 5
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito pour simuler les objets complexes
import static org.mockito.Mockito.mock;

// Imports de tes classes (assure-toi que les chemins sont bons)
import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;
import com.lllmtb.events.game.*;
import com.lllmtb.events.move.*;
import com.lllmtb.events.graphicsUi.*;

import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.graphicsUi.ScreenGenerateMap;


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
        // eventWithoutArgument(new MoveDownEvent());
        // eventWithoutArgument(new MoveLeftEvent());
        // eventWithoutArgument(new MoveRightEvent());
        // eventWithoutArgument(new MoveUpEvent());
    }

  
    private void eventWithoutArgument(Event event) {
        assertNotNull(event);
        assertTrue(event instanceof Event);
    }
}