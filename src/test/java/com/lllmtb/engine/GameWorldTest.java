package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.terrain.generation.GenerateMap;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import com.lllmtb.terrain.generation.LandScape;

/**
 * Tests for GameWorld with minimal Mockito usage.
 * - Only MapLoader.loadMatrix(...) (static) is mocked when needed.
 * - Uses real GenerateMap, PhysicsEngine, GameLoop, Team, Worm implementations.
 *
 * IMPORTANT: these tests assume the other classes (PhysicsEngine, Team, Worm, GenerateMap, MapLoader...)
 * exist in the project and behave sensibly (construction doesn't blow up).
 */
public class GameWorldTest {

    @AfterEach
    void tearDown() {
        // nothing to tear down globally
    }

    @Test
    void whenMapFileNameIsNull_thenGenerateMapCreatesMap_and_teamsAndWormsRegistered() {
        // Use floatingIsland = false (WORLD)
        GameSettings settings = new GameSettings(
                1,                 // nbTeams
                new String[]{"TeamA"}, // teamNames
                2,                 // nbWorms
                false,             // hasWind
                null,              // mapFileName -> will use GenerateMap.createMap(WORLD)
                false   ,           // floatingIsland
                370,    //widthMap
                LandScape.HILLY     //heightMap
        );
        EventBus eventBus = new EventBus();

        // Construct GameWorld using real GenerateMap + real PhysicsEngine
        GameWorld gw = new GameWorld(settings,eventBus);

        // Map should be set (GenerateMap must have returned a map)
        Map map = gw.getMap();
        assertNotNull(map, "GenerateMap should produce a non-null Map when mapFileName == null");

        // EventBus present
        assertNotNull(gw.getBus());

        // Physics engine present and should contain bodies equal to nbWorms (2) for one team
        PhysicsEngine physics = gw.getPhysics();
        assertNotNull(physics);

        // getTeams should contain one team named TeamA
        List<Team> teams = gw.getTeams();
        assertEquals(1, teams.size());
        assertEquals("TeamA", teams.get(0).name);

        // Physics engine should contain nbWorms bodies (the worms created during constructor)
        int bodies = physics.getBodies().size();
        assertEquals(2, bodies, "Physics engine should have 2 bodies (2 worms registered)");
    }

    @Test
    void whenMapFileNameProvided_MapLoaderIsUsed_insteadOfGenerateMap() {
        // we only mock MapLoader.loadMatrix to return a real map created by GenerateMap
        Map generated = MapLoader.loadMatrix("mapTest");
        GameSettings settings = new GameSettings(0, new String[]{}, 0, true, "mapTest", false,370,LandScape.HILLY);
        EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        // Map returned by MapLoader should be used
        assertNotNull(generated);
        assertNotNull(gw.getMap());
        assertEquals(generated.toString(), gw.getMap().toString());
        // Physics engine exists and has empty bodies (no worms)
        assertNotNull(gw.getPhysics());
        assertEquals(0, gw.getPhysics().getBodies().size());
    }

    @Test
    void whenMapLoaderThrows_constructorWrapsAndRethrowsRuntimeException() {
        try (MockedStatic<MapLoader> mocked = Mockito.mockStatic(MapLoader.class)) {
            mocked.when(() -> MapLoader.loadMatrix("badPath")).thenThrow(new IllegalStateException("io fail"));

            GameSettings settings = new GameSettings(
                    0, new String[]{}, 0, false, "badPath", false,370,LandScape.HILLY);
            EventBus eventBus = new EventBus();
            RuntimeException e = assertThrows(RuntimeException.class, () -> new GameWorld(settings,eventBus));
            assertNotNull(e.getCause());
            assertInstanceOf(IllegalStateException.class, e.getCause());
            assertEquals("io fail", e.getCause().getMessage());
        }
    }

    @Test
    void teams_loop_breaks_on_ArrayIndexOutOfBounds_and_only_validTeamsCreated() {
        // nbTeams = 3 but only one name provided -> GameWorld should create only the first team then break
        GameSettings settings = new GameSettings(
                3,
                new String[]{"SoloTeam"},
                3,      // 3 worms for the created team
                true,
                null,   // use GenerateMap
                true,    // floating island -> FLYINGWORLD
                370,
                LandScape.HILLY
        );
         EventBus eventBus = new EventBus();

        GameWorld gw = new GameWorld(settings,eventBus);

        // Only one team created (index 0) and 3 worms registered for that team -> physics bodies = 3
        List<Team> teams = gw.getTeams();
        assertEquals(1, teams.size(), "Should only create one team (second iteration triggers ArrayIndexOutOfBounds and breaks)");
        assertEquals("SoloTeam", teams.get(0).name);

        assertEquals(3, gw.getPhysics().getBodies().size(), "Physics should contain 3 worm bodies for the single team");
    }

    @Test
    void startRunsLoopAndStopStopsIt_whenStartedInSeparateThread() throws Exception {
        // use a small, simple world (no teams) so physics starts empty
        GameSettings settings = new GameSettings(0, new String[]{}, 0, false, null, false,370,LandScape.HILLY);
         EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        // run GameWorld.start() (test thread won't block)
        gw.start();
        assertTrue(gw.loopThread.isAlive());

        // Let the loop run for a short time so it enters at least one iteration
        TimeUnit.MILLISECONDS.sleep(120);

        // Now stop the loop (GameWorld.stop delegates to GameLoop.stop)
        gw.stop();
        assertFalse(gw.loopThread.isAlive(), "Game loop thread should have terminated after stop()");

        // After stop, GameLoop.stop() clears bus and physics, so physics bodies should be empty
        assertTrue(gw.getPhysics().getBodies().isEmpty(), "Physics bodies should be cleared after stop()");
        // bus should exist and be cleared - calling processQueued should be safe (no exceptions)
        gw.getBus().processQueued();
    }

    @Test
    void registerEntity_null_throwsNPE() {
        GameSettings settings = new GameSettings(0, new String[]{}, 0, false, null, false,370,LandScape.HILLY);
         EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        assertThrows(NullPointerException.class, () -> gw.registerEntity(null));
    }

    @Test
    void stopLoop_whenNotRunning() {
        GameSettings settings = new GameSettings(0, new String[]{}, 0, false, null, false,370,LandScape.HILLY);
         EventBus eventBus = new EventBus();
        GameWorld gw = new GameWorld(settings,eventBus);

        assertNull(gw.loopThread);
        gw.stop();  // shouldn't do anything because loopThread == null
        assertNull(gw.loopThread);
    }

    @Test
    void startLoop_whenAlreadyRunning() throws Exception {
        GameSettings settings = new GameSettings(0, new String[]{}, 0, false, null, false,370,LandScape.HILLY);
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
    void getTurnManage(){
        GameSettings settings = new GameSettings(2, new String[]{}, 2, false, null, false);
        GameWorld gw = new GameWorld(settings);

        assertNotNull(gw.getTurnManager());
    }
}
