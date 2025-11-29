package com.lllmtb.physics;

import org.eclipse.jdt.annotation.NonNull;

@FunctionalInterface
public interface Transform {
    @NonNull
    Vector apply(@NonNull Vector v);
}
