package com.lllmtb.events;

import org.eclipse.jdt.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Lightweight event bus designed for the game loop with support for:
 * <ul>
 *     <li>immediate synchronous dispatch on the calling thread ({@link #postSync(Event)})</li>
 *     <li>queued dispatch processed by the game loop ({@link #postQueued(Event)} + {@link #processQueued()})</li>
 *     <li>subscribe/unsubscribe operations.</li>
 * </ul>
 *
 * <p>
 * <b>Notes:</b>
 * <ul>
 *   <li>The queued buffer is bounded and capacity is configurable through the constructor. {@link #postQueued(Event)}
 *   returns {@code false} when the buffer is full.</li>
 *   <li>Subscribers are stored in a {@link CopyOnWriteArraySet}, which makes iteration safe during dispatch and
 *   guarantees that handlers see a consistent snapshot.</li>
 *   <li>Dispatch currently matches listeners registered for the exact runtime class of the posted {@link Event}.
 *   It does not dispatch to listeners registered on superclasses or interfaces,
 *   but this behaviour could easily be implemented in the future</li>
 * </ul>
 */
public class EventBus {
    // Group the listeners by event type
    private final ConcurrentMap<Class<? extends Event>, CopyOnWriteArraySet<EventListener<? extends Event>>> listeners = new ConcurrentHashMap<>();

    // Queue for events posted from anywhere to be processed by the game loop
    // (bounded to avoid out-of-memory crashes)
    private final BlockingQueue<Event> queuedEvents;

    /**
     * Create an EventBus with a default queue capacity of 1000 events.
     */
    public EventBus() {
        this(1000);
    }

    /**
     * Create an EventBus with a given queue capacity.
     *
     * @param capacity the maximum number of queued events the bus will hold; must be > 0
     * @throws IllegalArgumentException if {@code capacity <= 0}
     */
    public EventBus(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("queue capacity must be > 0");
        this.queuedEvents = new LinkedBlockingQueue<>(capacity);
    }

    /**
     * Subscribe a listener for a specific event type.
     * <p>
     * The listener will be invoked when events of the given {@code eventType} are dispatched.
     *
     * @param eventType the class of events to listen for
     * @param listener  the listener to register
     * @param <T>       event type
     * @throws NullPointerException if {@code eventType} or {@code listener} is null
     */
    public <T extends Event> void subscribe(@NonNull Class<T> eventType, @NonNull EventListener<T> listener) {
        listeners
            .computeIfAbsent(Objects.requireNonNull(eventType), l -> new CopyOnWriteArraySet<>())
            .add(Objects.requireNonNull(listener));
    }

    /**
     * Unsubscribe a listener for a specific event type.
     *
     * @param eventType the class of events the listener was registered for
     * @param listener  the listener to remove
     * @param <T>       event type
     * @return {@code true} if the listener was found and removed, {@code false} otherwise
     * @throws NullPointerException if {@code eventType} or {@code listener} is null
     */
    public <T extends Event> boolean unsubscribe(@NonNull Class<T> eventType, @NonNull EventListener<T> listener) {
        Objects.requireNonNull(eventType);
        Objects.requireNonNull(listener);
        CopyOnWriteArraySet<EventListener<? extends Event>> set = listeners.get(eventType);
        return set != null && set.remove(listener);
    }

    /**
     * Post an event for immediate synchronous dispatch.
     * Prefer {@link #postQueued(Event)} when possible.
     *
     * @param event the event to dispatch
     * @throws NullPointerException if {@code event} is null
     */
    public void postSync(@NonNull Event event) {
        Objects.requireNonNull(event);
        dispatch(event);
    }

    /**
     * Post an event to the internal queue for later processing by {@link #processQueued()} in the main game engine.
     * If the queue is full, the event is not enqueued and the method returns {@code false}.
     *
     * @param event the event to enqueue
     * @return {@code true} if the event was accepted into the queue, {@code false} if the queue is full
     * @throws NullPointerException if {@code event} is null
     */
    public boolean postQueued(@NonNull Event event) {
        Objects.requireNonNull(event);
        return queuedEvents.offer(event);
    }

    /**
     * Drain and process queued events. Intended to be called from the game loop at deterministic points.
     * Events are processed in FIFO order.
     */
    public void processQueued() {
        List<Event> drained = new ArrayList<>();
        queuedEvents.drainTo(drained);
        for (Event event : drained) dispatch(event);
    }

    /**
     * Internal dispatch implementation. It invokes listeners registered for the exact runtime class of the event.
     *
     * @param event the event to dispatch
     */
    @SuppressWarnings("unchecked")
    private <T extends Event> void dispatch(@NonNull T event) {
        Objects.requireNonNull(event);

        // Look up listeners for the exact event class. If none exist, skip.
        //      Later on we can add a way to dispatch to listeners listening for a superclass of the event as well
        //      if we end up adding subclasses to events.
        //      Alternatively, we could register the listener in all compatible subclasses directly and keep the logic below
        CopyOnWriteArraySet<EventListener<? extends Event>> matching = listeners.get(event.getClass());
        if (matching == null || matching.isEmpty()) return;

        for (EventListener<? extends Event> listener : matching) {
            try {
                ((EventListener<T>) listener).handle(event); // safe to cast
            } catch (Exception ex) {
                // temporary error log until we implement a proper logging system
                ex.printStackTrace();
            }
        }
    }

    /**
     * Remove all listeners and clear the event queue. Use on shutdown or test cleanup.
     */
    public void clear() {
        listeners.clear();
        queuedEvents.clear();
    }
}
