package com.lllmtb.events;

import com.lllmtb.engine.logger.Logger;
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
   <li>Dispatch matches listeners registered for the exact event class as well as any superclasses.
   For example, if a listener is subscribed to a superclass type E, it will receive events posted as subclasses of E.</li>
 * </ul>
 */
public class EventBus {
    // Group the listeners by event type
    private final ConcurrentMap<Class<? extends Event>, CopyOnWriteArraySet<EventListener<? extends Event>>> listeners = new ConcurrentHashMap<>();

    // Queue for events posted from anywhere to be processed by the game loop
    // (bounded to avoid out-of-memory crashes)
    private final BlockingQueue<Event> queuedEvents;

    private boolean locked = false;

    /**
     * Create an EventBus with a default queue capacity of 1000 events.
     */
    public EventBus() {
        this(1000);
    }

    /**
     * Create an EventBus with a given queue capacity.
     * @param capacity the maximum number of queued events the bus will hold; must be > 0
     * @throws IllegalArgumentException if {@code capacity <= 0}
     */
    public EventBus(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("queue capacity must be > 0");
        }
        this.queuedEvents = new LinkedBlockingQueue<>(capacity);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Subscribe a listener for a specific event type.
     * <p>
     * The listener will be invoked when events of the given {@code eventType} are dispatched.
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
     * @param eventType the class of events the listener was registered for
     * @param listener  the listener to remove
     * @param <T>       event type
     * @return {@code true} if null or if the listener was found and removed, {@code false} otherwise
     * @throws NullPointerException if {@code eventType} is null
     */
    public <T extends Event> boolean unsubscribe(@NonNull Class<T> eventType, EventListener<T> listener) {
        if (listener == null) return true;
        CopyOnWriteArraySet<EventListener<? extends Event>> set = listeners.get(eventType);
        return set != null && set.remove(listener);
    }

    /**
     * Post an event for immediate synchronous dispatch.
     * Prefer {@link #postQueued(Event)} when possible.
     * @param event the event to dispatch
     * @throws NullPointerException if {@code event} is null
     */
    public void postSync(@NonNull Event event) {
        Objects.requireNonNull(event);
        if (locked) return;
        dispatch(event);
    }

    /**
     * Post an event to the internal queue for later processing by {@link #processQueued()} in the main game engine.
     * If the queue is full, the event is not enqueued and the method returns {@code false}.
     * @param event the event to enqueue
     * @return {@code true} if the event was accepted into the queue, {@code false} if the queue is full
     * @throws NullPointerException if {@code event} is null
     */
    public boolean postQueued(@NonNull Event event) {
        Objects.requireNonNull(event);
        if (locked) return false;
        return queuedEvents.offer(event);
    }

    /**
     * Drain and process queued events. Intended to be called from the game loop at deterministic points.
     * Events are processed in FIFO order.
     */
    public void processQueued() {
        List<Event> drained = new ArrayList<>();
        queuedEvents.drainTo(drained);
        for (Event event : drained) {
            dispatch(event);
        }
    }

    /**
     * Internal dispatch implementation. It invokes listeners registered for the exact runtime class of the event
     * as well as any superclasses of the event.
     * @param event the event to dispatch
     */
    @SuppressWarnings("unchecked")
    private <T extends Event> void dispatch(@NonNull T event) {
        Objects.requireNonNull(event);

        Class<?> eventClass = event.getClass();

        // Iterate through all registered listener types and check if they are assignable from the event class
        for (var entry : listeners.entrySet()) {
            Class<?> listenerType = entry.getKey();
            CopyOnWriteArraySet<EventListener<? extends Event>> listenerSet = entry.getValue();

            // Check if the event is an instance of (or subclass of) the listener's registered type
            if (listenerType.isAssignableFrom(eventClass)) {
                for (EventListener<? extends Event> listener : listenerSet) {
                    try {
                        ((EventListener<T>) listener).handle(event); // safe to cast
                    } catch (Exception ex) {
                        Logger.logger.error("Error while handling event {} with listener {} : ", event, listener);
                        Logger.logger.debug("Error while handling event {} with listener {} : {} {}", event, listener, ex, ex.getStackTrace());
                    }
                }
            }
        }
    }

    /**
     * @return the number of events in the queue
     */
    public int queuedEventCount() {
        return queuedEvents.size();
    }

    /**
     * @return the total number of registered listeners
     */
    public int listenerCount() {
        int sum = 0;
        for (CopyOnWriteArraySet<EventListener<? extends Event>> set : listeners.values()) {
            sum += set.size();
        }
        return sum;
    }

    /**
     * Remove all listeners and clear the event queue. Use on shutdown or test cleanup.
     */
    public void clear() {
        listeners.clear();
        queuedEvents.clear();
    }
}
