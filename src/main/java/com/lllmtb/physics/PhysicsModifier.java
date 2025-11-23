package com.lllmtb.physics;

@FunctionalInterface
public interface PhysicsModifier {
    Vector apply(Vector v);
}
