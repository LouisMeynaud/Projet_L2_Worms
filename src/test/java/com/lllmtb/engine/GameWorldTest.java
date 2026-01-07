package com.lllmtb.engine;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.lllmtb.engine.save.GameSave;
import com.lllmtb.engine.save.Load;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.terrain.generation.LandScape;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

public class GameWorldTest {

    @Test
    void whenMapFileNameIsNull_thenGenerateMapCreatesMap_and_teamsAndWormsRegistered() {
        // set mapFileName to null to trigger GenerateMap
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"TeamA"}, 2, 100, 10, false, null, false, 370, LandScape.HILLY, 2, GameMode.NORMAL, true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);
        assertNotNull(gw.getBus());

        assertEquals("blabla", gw.getGameName());

        // Map should be set (GenerateMap must have returned a map)
        Map map = gw.getMap();
        assertNotNull(map, "GenerateMap should produce a non-null Map when mapFileName == null");

        PhysicsEngine physics = gw.getPhysics();
        assertNotNull(physics);
        int bodies = physics.getBodies().size();
        assertEquals(2, bodies, "Physics engine should have 2 bodies (2 worms registered)");

        List<Team> teams = gw.getTeams();
        Team t = gw.getPlayingTeam();
        assertEquals(1, teams.size());
        assertEquals("TeamA", teams.get(0).name);
        assertEquals("TeamA", t.name);
        assertEquals(0, gw.getIndexTeam());
    }

    @Test
    void whenMapFileNameProvided_MapLoaderIsUsed_insteadOfGenerateMap() {
        // load the map directly and compare
        Map generated = MapLoader.loadMap("mapTest");
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, true, "mapTest", false,370,LandScape.HILLY,2,GameMode.NORMAL,true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        assertNotNull(generated);
        assertNotNull(gw.getMap());
        assertEquals(generated.toString(), gw.getMap().toString());

        assertNotNull(gw.getPhysics());
        assertEquals(1, gw.getPhysics().getBodies().size());
    }

    @Test
    void whenMapLoaderThrows_constructorWrapsAndRethrowsRuntimeException() {
        try (MockedStatic<MapLoader> mocked = Mockito.mockStatic(MapLoader.class)) {
            mocked.when(() -> MapLoader.loadMap("badPath")).thenThrow(new IllegalStateException("io fail"));

            GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1, 100,10, false, "badPath", false,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
            EventBus eventBus = new EventBus();
            RuntimeException e = assertThrows(RuntimeException.class, () -> new GameWorld(settings,eventBus));
            assertNotNull(e.getCause());
            assertInstanceOf(IllegalStateException.class, e.getCause());
            assertEquals("io fail", e.getCause().getMessage());
        }
    }
    @Test
    void constructor_withGameSave_Map() throws FileNotFoundException {
        Map map = MapLoader.loadMap("Camembert");
        assertNotNull(map);
        GameSave gameSave = Load.loadGameSave("Test");
        GameWorld gameWorld = new GameWorld(gameSave, map);
        assertNotNull(gameWorld);
        assertEquals("Test", gameWorld.getGameName());
    }

    @Test
    void teams_loop_breaks_on_ArrayIndexOutOfBounds_and_only_validTeamsCreated() {
        // nbTeams = 3 but only one name provided
        assertThrows(
                IllegalArgumentException.class,
                () -> new GameSettings("blabla", 3, new String[]{"SoloTeam"},3, 100, 10000, true, null, true,370, LandScape.HILLY, 2, GameMode.NORMAL, true, false)
        );
    }

    @Test
    void startRunsLoopAndStopStopsIt_whenStartedInSeparateThread() throws Exception {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, false, null, false,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        // run GameWorld.start() (test thread won't block)
        gw.start();
        assertTrue(gw.loopThread.isAlive());

        // let the loop run for a short time so it enters at least one iteration
        TimeUnit.MILLISECONDS.sleep(120);

        // now stop the loop
        gw.stop();
        assertFalse(gw.loopThread.isAlive(), "Game loop thread should have terminated after stop()");

        // GameLoop.stop() clears bus and physics
        assertTrue(gw.getPhysics().getBodies().isEmpty(), "Physics bodies should be cleared after stop()");
        // bus should exist and be cleared
        gw.getBus().processQueued();
    }

    @Test
    void registerEntity_null_throwsNPE() {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, false, null, false,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
         EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        assertThrows(NullPointerException.class, () -> gw.registerEntity(null));
    }

    @Test
    void stopLoop_whenNotRunning() {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, false, null, false,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
         EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        assertNull(gw.loopThread);
        gw.stop();  // shouldn't do anything because loopThread == null
        assertNull(gw.loopThread);
    }

    @Test
    void startLoop_whenAlreadyRunning() {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1,100, 10, false, null, false,370,LandScape.HILLY,2,GameMode.NORMAL, true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        gw.start();
        Thread first = gw.loopThread;
        int beforeCount = Thread.activeCount();

        gw.start(); // shouldn't create a new thread
        Thread second = gw.loopThread;
        int afterCount = Thread.activeCount();

        // Assert same object
        assertSame(first, second, "start() should not create a new thread if one is already running");

        // Assert the thread count doesn't go up
        assertEquals(beforeCount, afterCount, "start() must not create an additional running thread");

        // Assert it is running
        assertTrue(second.isAlive(), "Loop thread should still be alive after second start()");

        gw.stop();
    }

    @Test
    void pauseAndResume_togglesGameLoopPauseState() throws Exception {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"Team1"}, 1, 100,100, false, null, false, 370, LandScape.HILLY, 2,GameMode.NORMAL, true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);

        gw.start();
        assertTrue(gw.loopThread.isAlive(), "Game loop should be running");
        TimeUnit.MILLISECONDS.sleep(50);

        gw.pause(true);
        assertTrue(gw.loopThread.isAlive(), "Game loop thread should still be alive while paused");
        long ticksBeforePause = gw.getGameLoop().getTicks();
        assertTrue(ticksBeforePause > 0, "Game loop should have ticked while running");
        TimeUnit.MILLISECONDS.sleep(100);
        long ticksAfterPaused = gw.getGameLoop().getTicks();
        assertEquals(ticksBeforePause, ticksAfterPaused, "Ticks should not increment while paused");


        gw.pause(false);
        assertTrue(gw.loopThread.isAlive(), "Game loop thread should still be alive after resume");
        TimeUnit.MILLISECONDS.sleep(50);
        long ticksAfterResume = gw.getGameLoop().getTicks();
        assertTrue(ticksAfterResume > ticksAfterPaused, "Ticks should resume incrementing after pause is lifted");

        gw.stop();
    }

    @Test
    void getTurnManager() {
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"José"}, 1, 100, 100, false, null, false, 370, LandScape.HILLY, 2, GameMode.NORMAL, true, false);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings, eventBus);

        assertNotNull(gw.getTurnManager());
    }
}
