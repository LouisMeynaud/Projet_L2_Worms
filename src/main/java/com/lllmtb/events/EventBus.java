package com.lllmtb.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class EventBus {
    // group the listeners by event
    private final ConcurrentMap<Class<? extends Event>, CopyOnWriteArraySet<EventListener<? extends Event>>> listeners = new ConcurrentHashMap<>();

    // queue for events posted from any thread to be processed on the game loop
    // bounded to avoid out-of-memory crashes
    private final BlockingQueue<Event> queuedEvents;

    public EventBus() {
        this(1000);
    }

    public EventBus(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("queue capacity must be > 0");
        this.queuedEvents = new LinkedBlockingQueue<>(capacity);
    }

    // register a listener
    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners
            .computeIfAbsent(Objects.requireNonNull(eventType), l -> new CopyOnWriteArraySet<>())
            .add(Objects.requireNonNull(listener));
    }

    // remove a listener
    public <T extends Event> boolean unsubscribe(Class<T> eventType, EventListener<T> listener) {
        CopyOnWriteArraySet<EventListener<? extends Event>> set = listeners.get(eventType);
        return set != null && set.remove(listener);
    }

    // post for immediate synchronous dispatch in calling thread
    public void postSync(Event event) {
        dispatch(event);
    }

    // post for queued processing on the main game thread (deterministic)
    public boolean postQueued(Event event) {
        Objects.requireNonNull(event);
        return queuedEvents.offer(event);
    }

    // called by the game loop to process queued events at a safe point
    public void processQueued() {
        List<Event> drained = new ArrayList<>();
        queuedEvents.drainTo(drained);
        for (Event event : drained) dispatch(event);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void dispatch(T event) {
        Objects.requireNonNull(event);

        // later on we can add a way to dispatch to listeners listening for a superclass of the event as well
        // if we end up adding subclasses to events.
        // alternatively, we could register the listener in all compatible subclasses directly and keep the logic below
        CopyOnWriteArraySet<EventListener<? extends Event>> matching = listeners.get(event.getClass());
        if (matching.isEmpty()) return;
        for (EventListener<? extends Event> listener : matching) {
            try {
                ((EventListener<T>) listener).handle(event);
            } catch (Exception ex) {
                ex.printStackTrace();   // temporary error log until we implement a proper logging system
            }
        }
    }

    public void clear() {
        listeners.clear();
        queuedEvents.clear();
    }
}