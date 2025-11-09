package com.lllmtb.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
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
}
