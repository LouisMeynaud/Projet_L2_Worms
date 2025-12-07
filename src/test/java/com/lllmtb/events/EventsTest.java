package com.lllmtb.events;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
import com.lllmtb.events.ui.*;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.events.ui.graphical.ScreenGenerateMap;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadMap;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.ui.graphical.GamePanel.Direction;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.physics.Position;


public class EventsTest {

  

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
    public void testCreationMouseInputEvent() {
        
        Position positionMock = mock(Position.class);
        double tempsTest = 1.5;
        Direction directionTest = Direction.Left;
        MouseInputEvent event = new MouseInputEvent(positionMock, tempsTest, directionTest);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertSame(positionMock, event.getPos(), "La position stockée doit être celle donnée");
        assertEquals(tempsTest, event.getTime(), "Le temps stocké doit être 1.5");
        assertEquals(directionTest, event.getBouton(), "La direction stockée doit être Left");
    }

   @Test
    public void testCreationMouseWheelEvent() {
        Direction directionTest = Direction.Up;
        MouseWheelMoveInputEvent event = new MouseWheelMoveInputEvent(directionTest);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(directionTest, event.direction, "La direction stockée doit être celle donnée");
    }

    @Test
    public void testCreationMoveEvent() {
        Direction directionTest = Direction.Right;
        MoveEvent event = new MoveEvent(directionTest);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(directionTest, event.direction, "La direction doit être 'Right'");
    }

    @Test
    public void WeaponSelectedEventTest() {
        WeaponId mockWeaponId = mock(WeaponId.class);
        WeaponSelectedEvent event = new WeaponSelectedEvent(mockWeaponId);
        assertNotNull(event, "L'evenement ne doit pas etre null");
        assertSame(mockWeaponId, event.getWeaponId(), "L'ID de l'arme La direction stockée doit être celle donnée");
    }

    @Test
    public void eventWithoutArgumentTest() {
        eventWithoutArgument(new GameStateUpdatedEvent());
        eventWithoutArgument(new ScreenGenerateMap());
        eventWithoutArgument(new ScreenHomeEvent());
        eventWithoutArgument(new ScreenLoadMap());
        eventWithoutArgument(new ScreenNewOrLoadEvent());
        eventWithoutArgument(new ScreenSettingsGame());
        eventWithoutArgument(new MapUpdateEvent());
        eventWithoutArgument(new RequestFocus());
        eventWithoutArgument(new ShootMode());
    }

  
    private void eventWithoutArgument(Event event) {
        assertNotNull(event);
        assertTrue(event instanceof Event);
    }
}