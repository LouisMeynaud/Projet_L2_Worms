package com.lllmtb.physics;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A functional interface representing a transformation applied to a vector.
 */
@FunctionalInterface
public interface Transform {
    @NonNull
    Vector apply(@NonNull Vector v);
}
