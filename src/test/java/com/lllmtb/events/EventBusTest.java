package com.lllmtb.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EventBus and EventListener.
 */
public class EventBusTest {

    private EventBus bus;

    @BeforeEach
    void setUp() {
        bus = new EventBus(4);
    }

    // --- helper test events ------------------------------------------------

    private static final class SimpleEvent implements Event {
        final String payload;
        SimpleEvent(String payload) { this.payload = payload; }
    }

    private static final class OtherEvent implements Event {
        final long id;
        OtherEvent(long id) { this.id = id; }
    }

    // --- events for polymorphic dispatch tests -----

    private abstract static class ParentEvent implements Event {
        final String name;
        ParentEvent(String name) { this.name = name; }
    }

    private static class ChildEvent extends ParentEvent {
        ChildEvent(String name) { super(name); }
    }

    private static class AnotherChildEvent extends ParentEvent {
        AnotherChildEvent(String name) { super(name); }
    }

    private static class GrandchildEvent extends ChildEvent {
        GrandchildEvent(String name) { super(name); }
    }

    // --- constructors --------------------------------------------------------
    @Test
    void constructor_default() {
        assertDoesNotThrow(() -> new EventBus());
    }

    @Test
    void constructor_throwsIfNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new EventBus(-100));
    }

    // --- basic subscribe/postSync/unsubscribe --------------------------------

    @Test
    void subscribe_and_postSync_invokesListener() {
        AtomicInteger actual = new AtomicInteger(0);
        EventListener<SimpleEvent> listener = e -> actual.incrementAndGet();

        bus.subscribe(SimpleEvent.class, listener);
        bus.postSync(new SimpleEvent("x"));

        assertEquals(1, actual.get(), "listener should have been invoked once");
    }

    @Test
    void unsubscribe_removesListener() {
        AtomicInteger actual = new AtomicInteger(0);
        EventListener<SimpleEvent> listener = e -> actual.incrementAndGet();

        bus.subscribe(SimpleEvent.class, listener);
        boolean removed = bus.unsubscribe(SimpleEvent.class, listener);
        assertTrue(removed, "expected unsubscribe true for previously subscribed listener");

        // should not invoke since unsubscribed
        bus.postSync(new SimpleEvent("x"));
        assertEquals(0, actual.get(), "listener should not be invoked after unsubscribe");

        // unsubscribe again -> returns false
        boolean removedAgain = bus.unsubscribe(SimpleEvent.class, listener);
        assertFalse(removedAgain, "expected false when unsubscribing a listener not present");
    }

    // --- queued posting and ordering ----------------------------------------

    @Test
    void postQueued_and_processQueued_preservesOrder() {
        List<String> seen = Collections.synchronizedList(new ArrayList<>());
        bus.subscribe(SimpleEvent.class, e -> seen.add("S:" + e.payload));
        bus.subscribe(OtherEvent.class, e -> seen.add("O:" + e.id));

        assertTrue(bus.postQueued(new SimpleEvent("first")));
        assertTrue(bus.postQueued(new OtherEvent(42L)));
        assertTrue(bus.postQueued(new SimpleEvent("second")));

        // nothing processed yet until processQueued
        assertEquals(0, seen.size());

        // assert the event order is preserved when processing
        bus.processQueued();
        assertEquals(3, seen.size());
        assertEquals("S:first", seen.get(0));
        assertEquals("O:42", seen.get(1));
        assertEquals("S:second", seen.get(2));
    }

    @Test
    void postQueued_returnsFalse_whenQueueFull() {
        // create a bus with capacity 1 to make it easy to fill
        EventBus small = new EventBus(1);
        assertTrue(small.postQueued(new SimpleEvent("1")), "first offer should succeed");
        // next offer fails because not drained yet
        assertFalse(small.postQueued(new SimpleEvent("2")), "second offer should be rejected when queue full");
        // after draining, offers succeed again
        small.processQueued();
        assertTrue(small.postQueued(new SimpleEvent("3")), "offer should succeed after drain");
    }

    @Test
    void processQueued_drainsAndLeavesQueueEmpty() {
        assertTrue(bus.postQueued(new SimpleEvent("a")));
        assertTrue(bus.postQueued(new SimpleEvent("b")));
        assertEquals(2, bus.queuedEventCount());
        bus.processQueued();
        assertEquals(0, bus.queuedEventCount(), "queue should be empty after processing");
    }

    // --- behavior when no listeners -----------------------------------------

    @Test
    void dispatch_withNoListenersDoesNotThrow() {
        // no listeners registered for SimpleEvent
        assertDoesNotThrow(() -> bus.postSync(new SimpleEvent("x")));
        // queued also must not throw; processQueued should be safe
        assertTrue(bus.postQueued(new SimpleEvent("y")));
        assertDoesNotThrow(() -> bus.processQueued());
    }

    // --- listener exceptions do not prevent other listeners -------------------

    @Test
    void listenerExceptionDoesNotPreventOtherListeners() {
        AtomicBoolean secondInvoked = new AtomicBoolean(false);

        EventListener<SimpleEvent> throwing = e -> { throw new RuntimeException("boom"); };
        EventListener<SimpleEvent> second = e -> secondInvoked.set(true);

        bus.subscribe(SimpleEvent.class, throwing);
        bus.subscribe(SimpleEvent.class, second);

        // synchronous dispatch
        bus.postSync(new SimpleEvent("x"));
        assertTrue(secondInvoked.get(), "second listener should still be invoked even if first throws");

        // reset and test queued path
        secondInvoked.set(false);
        bus.postQueued(new SimpleEvent("y"));
        bus.processQueued();
        assertTrue(secondInvoked.get(), "second listener should be invoked for queued dispatch too");
    }

    // --- clear behavior -----------------------------------------------------

    @Test
    void clear_removesListenersAndClearsQueue() {
        AtomicInteger count = new AtomicInteger(0);
        EventListener<SimpleEvent> l = e -> count.incrementAndGet();
        bus.subscribe(SimpleEvent.class, l);

        assertTrue(bus.postQueued(new SimpleEvent("x")));
        // now clear before processing
        bus.clear();

        // listeners are removed; posting sync should not call
        bus.postSync(new SimpleEvent("y"));
        assertEquals(0, count.get(), "listeners were cleared so no invocation expected");

        // queue was cleared; processing does nothing
        bus.processQueued(); // should not throw
        assertEquals(0, bus.queuedEventCount(), "queue should remain empty after clear and process");
    }

    // --- multiple listeners for same event ---------------------------------

    @Test
    void multipleListenersForSameEventAreAllInvoked() {
        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);
        bus.subscribe(SimpleEvent.class, e -> a.incrementAndGet());
        bus.subscribe(SimpleEvent.class, e -> b.incrementAndGet());

        bus.postSync(new SimpleEvent("x"));
        assertEquals(1, a.get());
        assertEquals(1, b.get());
    }

    // --- null argument checks ------------------------------------------------

    @Test
    void subscribe_nullArgs_throwsNpe() {
        assertThrows(NullPointerException.class, () -> bus.subscribe(null, (EventListener<SimpleEvent>) e -> {}));
        assertThrows(NullPointerException.class, () -> bus.subscribe(SimpleEvent.class, null));
    }

    @Test
    void unsubscribe_nullArgs_throwsNpe() {
        assertThrows(NullPointerException.class, () -> bus.unsubscribe(null, (EventListener<SimpleEvent>) e -> {}));
        assertThrows(NullPointerException.class, () -> bus.unsubscribe(SimpleEvent.class, null));
    }

    @Test
    void postSync_nullEvent_throwsNpe() {
        assertThrows(NullPointerException.class, () -> bus.postSync(null));
    }

    @Test
    void postQueued_nullEvent_throwsNpe() {
        assertThrows(NullPointerException.class, () -> bus.postQueued(null));
    }

    // --- extra behavior tests ------------------------------------------------

    @Test
    void queuedEventCount_and_listenerCount() {
        EventBus b = new EventBus(10);
        assertEquals(0, b.listenerCount());
        assertEquals(0, b.queuedEventCount());

        b.subscribe(SimpleEvent.class, e -> {});
        assertEquals(1, b.listenerCount());

        b.postQueued(new SimpleEvent("z"));
        assertEquals(1, b.queuedEventCount());

        b.processQueued();
        assertEquals(0, b.queuedEventCount());
    }

    // --- ensure clear removes listeners and queue content ---------------------

    @Test
    void clear_onNonEmptyState_resetsEverything() {
        bus.subscribe(SimpleEvent.class, e -> {});
        bus.postQueued(new SimpleEvent("x"));
        assertTrue(bus.queuedEventCount() > 0);
        assertTrue(bus.listenerCount() > 0);

        bus.clear();
        assertEquals(0, bus.queuedEventCount());
        assertEquals(0, bus.listenerCount());
    }

    // --- polymorphic dispatch tests -----------------------------------------

    @Test
    void listenerForParentClass_receivesChildEvent() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ParentEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new ChildEvent("child1"));

        assertEquals(1, callCount.get(), "listener for ParentEvent should receive ChildEvent");
    }

    @Test
    void listenerForParentClass_receivesAnotherChildEvent() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ParentEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new AnotherChildEvent("other1"));

        assertEquals(1, callCount.get(), "listener for ParentEvent should receive AnotherChildEvent");
    }

    @Test
    void listenerForParentClass_receivesGrandchildEvent() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ParentEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new GrandchildEvent("grandchild1"));

        assertEquals(1, callCount.get(), "listener for ParentEvent should receive GrandchildEvent");
    }

    @Test
    void listenerForChildClass_receivesGrandchildEvent() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ChildEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new GrandchildEvent("grandchild1"));

        assertEquals(1, callCount.get(), "listener for ChildEvent should receive GrandchildEvent");
    }

    @Test
    void listenerForChildClass_doesNotReceiveAnotherChildEvent() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ChildEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new AnotherChildEvent("other1"));

        assertEquals(0, callCount.get(), "listener for ChildEvent should not receive AnotherChildEvent");
    }

    @Test
    void multipleListenersAtDifferentHierarchyLevels_allReceiveEvent() {
        AtomicInteger parentCount = new AtomicInteger(0);
        AtomicInteger childCount = new AtomicInteger(0);
        AtomicInteger grandchildCount = new AtomicInteger(0);

        bus.subscribe(ParentEvent.class, e -> parentCount.incrementAndGet());
        bus.subscribe(ChildEvent.class, e -> childCount.incrementAndGet());
        bus.subscribe(GrandchildEvent.class, e -> grandchildCount.incrementAndGet());

        bus.postSync(new GrandchildEvent("test"));

        assertEquals(1, parentCount.get(), "parent listener should be invoked");
        assertEquals(1, childCount.get(), "child listener should be invoked");
        assertEquals(1, grandchildCount.get(), "grandchild listener should be invoked");
    }

    @Test
    void polymorphicDispatch_works_withQueuedEvents() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ParentEvent.class, e -> callCount.incrementAndGet());

        assertTrue(bus.postQueued(new ChildEvent("child1")));
        assertTrue(bus.postQueued(new GrandchildEvent("grandchild1")));

        bus.processQueued();

        assertEquals(2, callCount.get(), "parent listener should receive both queued child events");
    }

    @Test
    void polymorphicDispatch_doesNotDispatchUpHierarchy() {
        AtomicInteger callCount = new AtomicInteger(0);
        bus.subscribe(ChildEvent.class, e -> callCount.incrementAndGet());

        bus.postSync(new ParentEvent("parent1") {});

        assertEquals(0, callCount.get(), "listener for child should not receive parent event");
    }

    @Test
    void exactClassMatch_stillWorks_withPolymorphicDispatch() {
        AtomicInteger childExactCount = new AtomicInteger(0);
        AtomicInteger parentCount = new AtomicInteger(0);

        bus.subscribe(ChildEvent.class, e -> childExactCount.incrementAndGet());
        bus.subscribe(ParentEvent.class, e -> parentCount.incrementAndGet());

        bus.postSync(new ChildEvent("child1"));

        // both should be invoked: exact match for ChildEvent and polymorphic match for ParentEvent
        assertEquals(1, childExactCount.get(), "listener for exact ChildEvent should be invoked");
        assertEquals(1, parentCount.get(), "listener for ParentEvent should also be invoked");
    }

    @Test
    void unsubscribe_worksCorrectly_withPolymorphicDispatch() {
        AtomicInteger callCount = new AtomicInteger(0);
        EventListener<ParentEvent> listener = e -> callCount.incrementAndGet();

        bus.subscribe(ParentEvent.class, listener);
        bus.postSync(new ChildEvent("child1"));
        assertEquals(1, callCount.get());

        // unsubscribe the parent listener
        boolean removed = bus.unsubscribe(ParentEvent.class, listener);
        assertTrue(removed, "listener should have been unsubscribed");

        // now posting a child event should not invoke the listener
        bus.postSync(new ChildEvent("child2"));
        assertEquals(1, callCount.get(), "listener should not be invoked after unsubscribe");
    }

    @Test
    void exceptionInPolymorphicDispatch_doesNotPreventOtherListeners() {
        AtomicBoolean secondInvoked = new AtomicBoolean(false);

        EventListener<ParentEvent> throwing = e -> { throw new RuntimeException("boom"); };
        EventListener<ParentEvent> second = e -> secondInvoked.set(true);

        bus.subscribe(ParentEvent.class, throwing);
        bus.subscribe(ParentEvent.class, second);

        bus.postSync(new ChildEvent("child1"));
        assertTrue(secondInvoked.get(), "second listener should be invoked despite first listener throwing");
    }
}
