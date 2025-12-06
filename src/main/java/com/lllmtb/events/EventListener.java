package com.lllmtb.events;

/**
 * Functional interface representing a handler for {@link Event} instances.
 *
 * <p>Implementations receive an event instance and perform side effects. Listeners can be implemented as:
 * <ul>
 *   <li>a lambda expression: {@code e -> ...}</li>
 *   <li>a method reference: {@code this::onEvent}</li>
 *   <li>an anonymous or named class implementing {@code EventListener}</li>
 * </ul>
 *
 * <p><b>Note:</b>
 * Exceptions thrown by handlers are the caller's responsibility to handle. The {@code EventBus}
 * will only log the error and continue dispatching to other listeners.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 * EventListener<MyEvent> l = e -> System.out.println("received " + e);
 * }</pre>
 * @param <T> concrete {@link Event} subtype handled by this listener
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    /**
     * Handle an event.
     * @param event the event instance (type {@code T})
     */
    void handle(T event);
}
