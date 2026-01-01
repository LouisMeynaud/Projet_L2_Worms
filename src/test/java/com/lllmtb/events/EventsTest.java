package com.lllmtb.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.turn.ChangeWormsEvent;
import com.lllmtb.events.game.CoinCollisionEvent;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.LoadEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.SaveEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.ToolSelectedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.physics.ExplosionEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.input.UseInputEvent;
import com.lllmtb.events.input.MoveInputEvent;
import com.lllmtb.events.input.JetpackBoostInputEvent;
import com.lllmtb.events.ui.graphical.MouseWheelMoveInputEvent;
import com.lllmtb.events.ui.graphical.ShootMode;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.events.ui.graphical.ScreenGenerateMap;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadGame;
import com.lllmtb.events.ui.graphical.ScreenLoadMap;
import com.lllmtb.events.ui.graphical.ScreenMusicSettingsEvent;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.graphical.GamePanel.Direction;

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
        UseInputEvent event = new UseInputEvent(positionMock, tempsTest);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertSame(positionMock, event.pos(), "La position stockée doit être celle donnée");
        assertEquals(tempsTest, event.time(), "Le temps stocké doit être 1.5");
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
        MoveInputEvent event = new MoveInputEvent(directionTest);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(directionTest, event.direction(), "La direction doit être 'Right'");
    }

    @Test
    public void weaponSelectedEventTest() {
        WeaponId mockWeaponId = mock(WeaponId.class);
        WeaponSelectedEvent event = new WeaponSelectedEvent(mockWeaponId);
        assertNotNull(event, "L'evenement ne doit pas etre null");
        assertSame(mockWeaponId, event.getWeaponId(), "L'ID de l'arme ");
    }

    @Test
    public void woolSelectedEventTest() {
        ToolId mockToolId = mock(ToolId.class);
        ToolSelectedEvent event = new ToolSelectedEvent(mockToolId);
        assertNotNull(event, "L'evenement ne doit pas etre null");
        assertSame(mockToolId, event.getToolId(), "L'ID de l'arme");
    }

    @Test
    public void testCreationLoadEvent() {
        String nom = "oui";
        LoadEvent event = new LoadEvent(nom);
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(nom, event.stringPath, "le stringpath doit etre: oui ");
    }

    @Test
    public void testCreationExplosionEvent() {
        Position position = new Position(10, 12);
        ExplosionEvent event = new ExplosionEvent(position, 15, "BazookaExplosion");
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(15, event.getRadius());
        assertEquals("BazookaExplosion", event.getAnimationKey());
        assertEquals(position, event.getPosition());
    }

    @Test
    public void CoinCollisionEventTest() {
        Coin mockCoin = mock(Coin.class);
        Team mockTeam = mock(Team.class);
        CoinCollisionEvent event = new CoinCollisionEvent(mockCoin, mockTeam);

        // On vérifie que tout est bien stocké
        assertNotNull(event, "L'événement ne doit pas être null");
        assertEquals(mockCoin, event.coin);
        assertEquals(mockTeam, event.team);
    }

    @Test
    void MapUpdateEvent() {
        Position center = new Position(100, 200);
        int radius = 50;
        MapUpdateEvent event = new MapUpdateEvent(center, radius);
        assertFalse(event.isFullUpdate());
        assertEquals(center, event.getCenter());
        assertEquals(radius, event.getRadius());
    }

    @Test
    public void eventsWithoutArgumentsTest() { // for JaCoCo - these events are just empty triggers
        new GameStateUpdatedEvent();
        new ScreenGenerateMap();
        new ScreenHomeEvent();
        new ScreenLoadMap();
        new ScreenNewOrLoadEvent();
        new ScreenSettingsGame();
        new ScreenMusicSettingsEvent();
        new MapUpdateEvent();
        new SaveEvent();
        new RequestFocus();
        new ShootMode();
        new TurnSkipEvent();
        new ScreenLoadGame();
        new JetpackBoostInputEvent();
        new ChangeWormsEvent();
    }

    @Test
    void PhysicsCollisionWithOtherEntityEventTest() {
        Entity movingBodyMock = mock(com.lllmtb.model.entity.Entity.class);
        Entity hitBodyMock = mock(com.lllmtb.model.entity.Entity.class);

        //TODO Collision with other entity maybe another form

//        assertNotNull(event);
//        assertSame(movingBodyMock, event.getMovingBody());
//        assertSame(hitBodyMock, event.getHitBody());
    }
}
