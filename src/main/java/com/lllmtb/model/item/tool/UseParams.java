package com.lllmtb.model.item.tool;

import com.lllmtb.model.entity.worm.Worm;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class UseParams {
    public final Worm owner;

    /**
     * Creates a new {@code FireParams} instance.
     * @param owner the worm that initiated the firing action (must not be {@code null})
     * @exception NullPointerException if {@code spawnPos} or {@code owner} is {@code null}
     */
    public UseParams(@NonNull Worm owner) {
        this.owner = Objects.requireNonNull(owner);
    }
}
