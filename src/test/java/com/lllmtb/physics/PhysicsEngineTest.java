package com.lllmtb.physics;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.physics.PhysicsModifierAddedEvent;
import com.lllmtb.events.physics.PhysicsModifierRemovedEvent;
import com.lllmtb.model.entity.projectile.BulletProjectile;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {

    private Map map;
    private EventBus bus;
    private PhysicsEngine pE;
    private Position p;
    private Worm w;
    private PhysicsBody pB;

    @BeforeEach
    void setUp() {
        map = MapLoader.loadMatrix("MapTestCollision");
        bus = new EventBus(4);
        pE = new PhysicsEngine(map, bus, true);
        p = new Position(4, 5);
        w = new Worm("José", new Team("Team", 1, new Position[]{p}));
        assertTrue(map.inMap(p));
        pB = w.getBody();
        pB.setVector(new Vector(-5, -10));
        pE.add(pB);
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
        PhysicsEngine tpE = new PhysicsEngine(map, bus, false);
        tpE.add(pB);

        Vector before = tpE.getWind();
        tpE.changeWind();
        Vector after = tpE.getWind();
        assertTrue(before.equals(after));
    }

    @Test
    void testGravityGetter() {
        Vector gravity = pE.getGravity();
        assertEquals(0, gravity.getX());
        assertEquals(-9.81, gravity.getY());
    }

    @Test
    void testAddAndRemove() {
        assertTrue(pE.getBodies().contains(pB));
        pE.remove(pB);
        assertFalse(pE.getBodies().contains(pB));
    }

    @Test
    void testClear() {
        pE.clear();
        assertTrue(pE.getBodies().isEmpty());
    }

    @Test
    void testNextStep() {
        double ticktime = 0.0167;
        Vector initialVector = pB.getVector();
        Position initialPos = pB.getPos();
        pE.nextStep(ticktime);
        Position newPos = pB.getPos();
        Vector newVector = pB.getVector();
        assertEquals(initialPos.getX(), newPos.getX());
        assertEquals(initialPos.getY(), newPos.getY());
        assertNotEquals(initialVector.getX(), newVector.getX());
        assertNotEquals(initialVector.getY(), newVector.getY());
    }

    @Test
    void testNextStep_whenCollision() {
        double ticktime = 0.0167;
        PhysicsEngine tpE = new PhysicsEngine(map, bus, false);
        tpE.add(pB);
        System.out.println(map);
        for (int i = 0; i < 605; i++) {
            // System.out.printf("\nPOS : %s, %s | inMap = %b", pB.getPos().getX(), pB.getPos().getY(), map.inMap(pB.getPos()));
            tpE.nextStep(ticktime);
        }
    }

    @Test
    void testNextStep_whenProjectile() {
        double ticktime = 0.0167;
        Projectile pr = new BulletProjectile(new FireParams(0, p, w));
        pE.add(pr.getBody());
        pE.nextStep(ticktime);
    }

    @Test
    void testWindChangesEvery600Ticks() {
        double ticktime = 0.0167;
        Vector windBefore = pE.getWind();
        for (int i = 0; i < 605; i++) {
            pE.next(ticktime);
        }
        Vector windAfter = pE.getWind();
        assertNotEquals(windBefore.getX(), windAfter.getX());
        assertNotEquals(windBefore.getY(), windAfter.getY());
    }

    @Test
    void testNext() {
        double ticktime = 0.0167;
        pE.next(ticktime);
        assertTrue(pB.getPos().getX() < 10);
    }

    @Test
    void addsModifierPublishesEventAndTransformsVector() {
        AtomicReference<PhysicsModifierAddedEvent> captured = new AtomicReference<>();
        bus.subscribe(PhysicsModifierAddedEvent.class, captured::set);

        Transform t = v -> new Vector(v.getX() + 1, v.getY() + 2);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);

        pE.addModifier(pB, m);

        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();

        PhysicsModifierAddedEvent ev = captured.get();
        assertNotNull(ev);
        assertSame(pB, ev.body());
        assertSame(m, ev.modifier());

        Vector in = new Vector(1, 1);
        Vector out = pE.applyModifiers(pB, in, 0.0167);
        assertEquals(2, out.getX());
        assertEquals(3, out.getY());
    }

    @Test
    void removingModifierPublishesEventAndStopsAffectingVector() {
        AtomicReference<PhysicsModifierRemovedEvent> captured = new AtomicReference<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, captured::set);

        Transform t = v -> new Vector(v.getX() * 2, v.getY() * 2);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);

        pE.addModifier(pB, m);
        bus.processQueued(); // clear add event

        Vector base = new Vector(2, 3);
        Vector afterApply = pE.applyModifiers(pB, base, 0.0167);
        assertEquals(4, afterApply.getX());
        assertEquals(6, afterApply.getY());

        pE.removeModifier(pB, m);
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();

        PhysicsModifierRemovedEvent ev = captured.get();
        assertNotNull(ev);
        assertSame(pB, ev.body());
        assertSame(m, ev.modifier());

        Vector afterRemoval = pE.applyModifiers(pB, base, 0.0167);
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

        pE.addModifier(pB, m1);
        pE.addModifier(pB, m2);
        bus.processQueued(); // clear add events

        pE.removeModifierFor(pB, source);
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

        pE.addModifier(pB, oneShot);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pB, start, 0.0167);
        assertEquals(5, afterFirst.getX());
        assertEquals(-3.0, afterFirst.getY());

        // one-shot should have been removed and a removed event posted
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(1, removed.size());
        assertSame(oneShot, removed.get(0).modifier());

        // second application should no longer change the vector
        Vector afterSecond = pE.applyModifiers(pB, start, 0.0167);
        assertEquals(5, afterSecond.getX());
        assertEquals(-10, afterSecond.getY());
    }

    @Test
    void timedModifierAppliedWhileEnabledThenRemovedAndPublishesRemovedEvent() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        Transform t = v -> new Vector(v.getX(), -3.0);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, 0.1, true);

        pE.addModifier(pB, m);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pB, start, 0.05);  // apply with delta time less than duration
        assertEquals(5, afterFirst.getX());
        assertEquals(-3.0, afterFirst.getY());

        // modifier should still be present, no removed event yet
        assertEquals(0, bus.queuedEventCount());

        Vector afterSecond = pE.applyModifiers(pB, start, 0.06);  // apply with delta time greater than remaining duration
        assertEquals(5, afterSecond.getX());
        assertEquals(-3.0, afterSecond.getY());

        // modifier should have been removed and a removed event posted
        assertEquals(1, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(1, removed.size());
        assertSame(m, removed.get(0).modifier());

        // third application should no longer change the vector
        Vector afterThird = pE.applyModifiers(pB, start, 0.0167);
        assertEquals(5, afterThird.getX());
        assertEquals(-10, afterThird.getY());
    }

    @Test
    void disabledModifierNotAppliedAndNotRemoved() {
        List<PhysicsModifierRemovedEvent> removed = new CopyOnWriteArrayList<>();
        bus.subscribe(PhysicsModifierRemovedEvent.class, removed::add);

        Transform t = v -> new Vector(v.getX(), -3.0);
        PhysicsModifier m = new PhysicsModifier(t, ToolId.PARACHUTE, 0.05, false);

        pE.addModifier(pB, m);
        bus.processQueued(); // clear add event

        Vector start = new Vector(5, -10);
        Vector afterFirst = pE.applyModifiers(pB, start, 0.1);  // apply with delta time greater than duration
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

        pE.removeModifier(pB, notAdded);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifierDoesNotPublishEventOrThrow_whenModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        Transform t = v -> new Vector(v.getX(), v.getY());
        PhysicsModifier added = new PhysicsModifier(t, WeaponId.GUN, 100, true);

        pE.addModifier(pB, added);
        bus.processQueued(); // clear add event

        PhysicsModifier notAdded = new PhysicsModifier(t, ToolId.PARACHUTE, Double.POSITIVE_INFINITY, true);
        pE.removeModifier(pB, notAdded);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifiersForDoesNotPublishEventOrThrow_whenNoModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        pE.removeModifierFor(pB, ToolId.PARACHUTE);
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }

    @Test
    void removeNonExistingModifiersForDoesNotPublishEventOrThrow_whenModifiersForBody() {
        AtomicInteger removedCount = new AtomicInteger(0);
        bus.subscribe(PhysicsModifierRemovedEvent.class, e -> removedCount.incrementAndGet());

        Transform t = v -> new Vector(v.getX(), v.getY());
        PhysicsModifier added = new PhysicsModifier(t, WeaponId.GUN, 100, true);

        pE.addModifier(pB, added);
        bus.processQueued(); // clear add event

        pE.removeModifierFor(pB, ToolId.PARACHUTE); // different source
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, removedCount.get());
    }
}

