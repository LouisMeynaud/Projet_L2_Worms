package com.lllmtb.physics;

@FunctionalInterface
public interface Transform {
    Vector apply(Vector v);
}
