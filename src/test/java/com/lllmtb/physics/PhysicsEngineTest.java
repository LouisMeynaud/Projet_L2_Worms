package com.lllmtb.physics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.Drop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.turn.TurnManager;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.physics.PhysicsModifierAddedEvent;
import com.lllmtb.events.physics.PhysicsModifierRemovedEvent;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;

class PhysicsEngineTest {
    private Map map;
    private GameWorld world;
    private PhysicsEngine pE;
    private PhysicsBody pBWorm;
    private PhysicsBody pBBP;
    private EventBus bus;

    @BeforeEach
    void setUp() {
        map = MapLoader.loadMatrix("mapTest");
        bus = new EventBus(4);
        world = Mockito.mock(GameWorld.class);
        Mockito.when(world.getBus()).thenReturn(bus);
        pE = new PhysicsEngine(map, world, true, GameMode.NORMAL);
        Position pw = new Position(4, 4);
        Worm w = new Worm("José", new Team("Team", 1, new Position[]{pw}, 0));
        assertTrue(map.inMap(pw));
        Position pBP = new Position(0, 5);
        BulletProjectile bP = new BulletProjectile(new UseParams(0, pBP, w, 0));
        assertTrue(map.inMap(pw));
        pBWorm = w.getBody();
        pBBP = bP.getBody();
        pBWorm.setVector(new Vector(-5, -10));
        pBWorm.setPos(pw);
        pBBP.setVector(new Vector(0, -4));
        pBBP.setPos(pBP);
        pE.add(pBWorm);
        pE.add(pBBP);
    }

    @Test
    void testGetWindInitial() {
        Vector wind = pE.getWind();
        assertNotNull(wind);
        assertEquals(0, wind.getX());
        assertEquals(0, wind.getY());
    }

    @Test
    void testHasWind() {
        assertTrue(pE.hasWind());
    }

    @Test
    void testChangeWind_whenWind() {
        Vector before = pE.getWind();
        pE.changeWind();
        Vector after = pE.getWind();
        assertNotEquals(before.getX(), after.getX());
        assertNotEquals(before.getY(), after.getY());
    }

    @Test
    void testChangeWind_whenNoWind() {
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        tpE.add(pBWorm);

        Vector before = tpE.getWind();
        tpE.changeWind();
        Vector after = tpE.getWind();
        assertTrue(before.equals(after));
    }

    @Test
    void testGravityGetter() {
        Vector gravity = pE.getGravity();
        assertEquals(0, gravity.getX());
        assertEquals(-9810.0, gravity.getY());
    }

    @Test
    void testAddAndRemove() {
        assertTrue(pE.getBodies().contains(pBWorm));
        pE.remove(pBWorm);
        assertFalse(pE.getBodies().contains(pBWorm));
    }

    @Test
    void testClear() {
        pE.clear();
        assertTrue(pE.getBodies().isEmpty());
    }

    @Test
    void testNextStep_WhenNoCollisions() {
        double dt = 0;
        PhysicsEngine tpE = new PhysicsEngine(map, world, false,GameMode.NORMAL);
        tpE.add(pBBP);
        pBBP.setPos(new Position(0, 5));
        pBBP.setVector(new Vector(0, 0));
        tpE.nextStep(dt);
        assertEquals(5, pBBP.getPos().getY());
        assertEquals(0, pBBP.getPos().getX());
    }

    @Test
    void testNextStep_WhenDie() {
        double dt = 0.0167;
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        tpE.add(pBBP);
        pBBP.setPos(new Position(0, 5));
        pBBP.setVector(new Vector(-10, 0));
        tpE.nextStep(dt);
        assertEquals(5, pBBP.getPos().getY());
        assertEquals(0.002, pBBP.getPos().getX());
    }

    @Test
    void testNextStep() {
        double dt = 0.0167;
        Vector initialVector = pBWorm.getVector();
        Position initialPos = pBWorm.getPos();
        pE.nextStep(dt);
        Position newPos = pBWorm.getPos();
        Vector newVector = pBWorm.getVector();
        assertEquals(3.91650697225, newPos.getX());
        assertEquals(4.002, newPos.getY());
        assertNotEquals(initialVector.getX(), newVector.getX());
        assertNotEquals(initialVector.getY(), newVector.getY());
    }

    @Test
    void testNextStep_whenCollision() {
        double dt = 0.0167;
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        tpE.add(pBWorm);
        tpE.add(pBBP);
        tpE.nextStep(dt);
        assertEquals(0.0, pBBP.getPos().getX());
        assertEquals(4.91678788128, pBBP.getPos().getY());
        assertEquals(3.91650697225, pBWorm.getPos().getX());
        assertEquals(4.002, pBWorm.getPos().getY());
    }

    @Test
    void testNextStep_whenCollision2() {
        double dt = 0.0167;
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        pBWorm.setVector(new Vector(0, 0));
        pBWorm.setPos(new Position(2, 4));
        pBBP.setPos(new Position(1, 5));
        pBBP.setVector(new Vector(5, 0));
        tpE.add(pBWorm);
        tpE.add(pBBP);
        tpE.nextStep(dt);
        assertEquals(1.000002000788798, pBBP.getPos().getX());
        assertEquals(4.999999606640421, pBBP.getPos().getY());
        assertEquals(2.0, pBWorm.getPos().getX());
        assertEquals(4.002, pBWorm.getPos().getY());
    }

    @Test
    void testNextStep_dropOnOtherDrop() {
        Drop drop1 = new Drop(new Position(0, 4), ToolId.PARACHUTE);
        Drop drop2 = new Drop(new Position(0, 6), ToolId.PARACHUTE);
        PhysicsBody body1 = drop1.getBody();
        PhysicsBody body2 = drop2.getBody();
        body1.setVector(new Vector(0, -3));
        body2.setVector(new Vector(0, -3));
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        tpE.add(body1);
        tpE.add(body2);
        double dt = 1.0;
        tpE.nextStep(dt);
        assertEquals(0.0, body1.getPos().getX());
        assertEquals(4.0, body1.getPos().getY());
        assertEquals(0.0, body2.getPos().getX());
        assertEquals(5.436491673103708, body2.getPos().getY());
    }

    @Test
    void testNextStep_coinOnWorm() {
        Coin coin = new Coin(new Position(4, 6), 10);
        PhysicsBody coinBody = coin.getBody();
        coinBody.setVector(new Vector(0, -3));
        pBWorm.setVector(new Vector(0, 0));
        PhysicsEngine tpE = new PhysicsEngine(map, world, false, GameMode.NORMAL);
        tpE.add(pBWorm);
        tpE.add(coinBody);
        double dt = 1.0;
        tpE.nextStep(dt);
        assertEquals(4.0, pBWorm.getPos().getX());
        assertEquals(4.002, pBWorm.getPos().getY());
        assertFalse(tpE.getBodies().contains(coinBody));
    }

    @Test
    void testWindChangesEvery1250Ticks() {
        double ticktime = 16.7;
        TurnManager t = new TurnManager(world, 10000);
        Map map = Mockito.mock(Map.class);
        Mockito.when(map.getWidth()).thenReturn(100);
        Mockito.when(map.getHeightWater()).thenReturn(0);
        Team team = new Team("TestTeam", 1, new Position[]{new Position(2,4)}, 0);
        Mockito.when(world.getTurnManager()).thenReturn(t);
        Mockito.when(world.getMap()).thenReturn(map);
        t.startTurn(team);
        Vector windBefore = pE.getWind();
        for (int i = 0; i < 1260; i++) {
            pE.next(ticktime);
        }
        Vector windAfter = pE.getWind();
        assertNotEquals(windBefore.getX(), windAfter.getX());
        assertNotEquals(windBefore.getY(), windAfter.getY());
    }

    @Test
    void testNext() {
        double ticktime = 16.7;
        pE.next(ticktime);
        assertTrue(pBWorm.getPos().getX() < 10);
    }

    @Test
    void addsModifierPublishesEventAndTransformsVector() {
        AtomicReference<PhysicsModifierAddedEvent> captured = new AtomicReference<>();
        bus.subscribe(PhysicsModifierAddedEvent.class, captured::set);

        Transform t = v -> new Vector(v.getX() + 1, v.getY() + 2);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);

        pE.addModifier(pBWorm, m);

        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();

        PhysicsModifierAddedEvent ev = captured.get();
        assertNotNull(ev);
        assertSame(pBWorm, ev.body());
        assertSame(m, ev.modifier());

        Vector in = new Vector(1, 1);
        Vector out = pE.applyModifiers(pBWorm, in, 0.0167);
        assertEquals(2, out.getX());
        assertEquals(3, out.getY());
    }

    @Test
    void removingModifierPublishesEventAndStopsAffectingVector() {
        AtomicReference<PhysicsModifierRemovedEvent> captured = new AtomicReference<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, captured::set);

        Transform t = v -> new Vector(v.getX() * 2, v.getY() * 2);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);

        pE.addModifier(pBWorm, m);
        bus.processQueued(); // clear add event

        Vector base = new Vector(2, 3);
        Vector afterApply = pE.applyModifiers(pBWorm, base, 0.0167);
        assertEquals(4, afterApply.getX());
        assertEquals(6, afterApply.getY());

        pE.removeModifier(pBWorm, m);
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();

        PhysicsModifierRemovedEvent ev = captured.get();
        assertNotNull(ev);
        assertSame(pBWorm, ev.body());
        assertSame(m, ev.modifier());

        Vector afterRemoval = pE.applyModifiers(pBWorm, base, 0.0167);
        assertEquals(2, afterRemoval.getX());
        assertEquals(3, afterRemoval.getY());
    }

    @Test
    void removeModifierForBySourcePublishesRemovedEventsForAllMatchingModifiers() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        ItemId source = ToolId.PARACHUTE;
        Transform t1 = v -> new Vector(0, 1);
        Transform t2 = v -> new Vector(1, 0);

        PhysicsModifier m1 = new PhysicsModifier(t1, source, Double.POSITIVE_INFINITY, true);
        PhysicsModifier m2 = new PhysicsModifier(t2, source, Double.POSITIVE_INFINITY, true);

        pE.addModifier(pBWorm, m1);
        pE.addModifier(pBWorm, m2);
        bus.processQueued(); // clear add events

        pE.removeModifierFor(pBWorm, source);
        assertEquals(2, bus.queuedEventCount());
        bus.processQueued();

        assertEquals(2, removed.size());
        Set<PhysicsModifier> removedModifiers = new HashSet<>();
        for (PhysicsModifierRemovedEvent e : removed) removedModifiers.add(e.modifier());
        assertTrue(removedModifiers.contains(m1));
        assertTrue(removedModifiers.contains(m2));
    }

    @Test
    void oneShotModifierAppliedOnceThenRemovedAndPublishesRemovedEvent() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        Transform t = v -> new Vector(v.getX(), -3.0); // parachute-like effect for test
        PhysicsModifier oneShot = new PhysicsModifier(t, ToolId.PARACHUTE);

        pE.addModifier(pBWorm, oneShot);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pBWorm, start, 0.0167);
        assertEquals(5, afterFirst.getX());
        assertEquals(-3.0, afterFirst.getY());

        // one-shot should have been removed and a removed event posted
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(1, removed.size());
        assertSame(oneShot, removed.get(0).modifier());

        // second application should no longer change the vector
        Vector afterSecond = pE.applyModifiers(pBWorm, start, 0.0167);
        assertEquals(5, afterSecond.getX());
        assertEquals(-10, afterSecond.getY());
    }

    @Test
    void timedModifierAppliedWhileEnabledThenRemovedAndPublishesRemovedEvent() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        Transform t = v -> new Vector(v.getX(), -3.0);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, 0.1, true);

        pE.addModifier(pBWorm, m);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pBWorm, start, 0.05);  // apply with delta time less than duration
        assertEquals(5, afterFirst.getX());
        assertEquals(-3.0, afterFirst.getY());

        // modifier should still be present, no removed event yet
        assertEquals(0, bus.queuedEventCount());

        Vector afterSecond = pE.applyModifiers(pBWorm, start, 0.06);  // apply with delta time greater than remaining duration
        assertEquals(5, afterSecond.getX());
        assertEquals(-3.0, afterSecond.getY());

        // modifier should have been removed and a removed event posted
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(1, removed.size());
        assertSame(m, removed.get(0).modifier());

        // third application should no longer change the vector
        Vector afterThird = pE.applyModifiers(pBWorm, start, 0.0167);
        assertEquals(5, afterThird.getX());
        assertEquals(-10, afterThird.getY());
    }

    @Test
    void disabledModifierNotAppliedAndNotRemoved() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        Transform t = v -> new Vector(v.getX(), -3.0);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, 0.05, false);

        pE.addModifier(pBWorm, m);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pBWorm, start, 0.1);  // apply with delta time greater than duration
        assertEquals(5, afterFirst.getX());
        assertEquals(-10, afterFirst.getY());

        // one-shot should have been removed and a removed event posted
        assertEquals(0, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(0, removed.size());
    }

    @Test
    void removeNonExistingModifierDoesNotPublishEventOrThrow_whenNoModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        Transform t = v -> new Vector(v.getX(), v.getY());
        PhysicsModifier notAdded = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);

        pE.removeModifier(pBWorm, notAdded);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifierDoesNotPublishEventOrThrow_whenModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        Transform t = v -> new Vector(v.getX(), v.getY());
        PhysicsModifier added = new PhysicsModifier(t, WeaponId.GUN, 100, true);

        pE.addModifier(pBWorm, added);
        bus.processQueued(); // clear add event

        PhysicsModifier notAdded = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);
        pE.removeModifier(pBWorm, notAdded);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifiersForDoesNotPublishEventOrThrow_whenNoModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        pE.removeModifierFor(pBWorm, ToolId.PARACHUTE);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifiersForDoesNotPublishEventOrThrow_whenModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        Transform t = v -> new Vector(v.getX(), v.getY());
        PhysicsModifier added = new PhysicsModifier(t, WeaponId.GUN, 100, true);

        pE.addModifier(pBWorm, added);
        bus.processQueued(); // clear add event

        pE.removeModifierFor(pBWorm, ToolId.PARACHUTE); // different source
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void testCreateExplosion() {


        Worm owner = (Worm) pBWorm.getEntity();
        Position pos = new Position(10, 10);
        RocketProjectile rocket = new RocketProjectile(new UseParams(0, pos, owner, 0));
        pE.add(rocket.getBody());
        pE.createExplosion(rocket, 10, 15, 30, 100, false);
        assertEquals(2, bus.queuedEventCount());//verifie que les event sont bien crée(explosion et mapUpdate)
    }

    @Test
    void fallDamageTest(){
        Team team = Mockito.mock(Team.class);
        Worm worm = new Worm("TestWorm", team);
        worm.getBody().setVector(new Vector(0, -100));
        PhysicsEngine.fallDamage(worm);
        //Worm has 100 HP and check if it loses some
        assertNotEquals(100, worm.getStats().getHp() );

    }

    @Test
    void checkIfWormDeathAfterFallDamage(){
        Team team = Mockito.mock(Team.class);
        Worm worm = new Worm("TestWorm", team);
        worm.damageWorm(90);
        assertTrue(worm.isAlive());
        worm.getBody().setVector(new Vector(0, -200));
        PhysicsEngine.fallDamage(worm);
        //Worm has 100 HP and check if it loses some
        assertFalse(worm.isAlive());

    }
}

