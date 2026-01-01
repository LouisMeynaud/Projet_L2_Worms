package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsModifier;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * A command that removes a physics modifier from a specified entity within the game world.
 */
public final class RemovePhysicsModifierCommand implements GameCommand {
    private final Entity target;
    private final PhysicsModifier modifier;

    public RemovePhysicsModifierCommand(@NonNull Entity target, @NonNull PhysicsModifier modifier) {
        this.target = Objects.requireNonNull(target);
        this.modifier = Objects.requireNonNull(modifier);
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().removeModifier(target.getBody(), modifier);
    }
}