package com.lllmtb.physics;

@FunctionalInterface
public interface Modifier {
    Vector apply(Vector v);
}
