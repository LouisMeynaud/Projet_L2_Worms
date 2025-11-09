package com.lllmtb.events;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void handle(T event);
}
