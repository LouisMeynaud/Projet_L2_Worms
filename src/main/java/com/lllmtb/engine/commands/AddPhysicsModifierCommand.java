package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsModifier;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * A command that adds a physics modifier to a specified entity within the game world.
 */
public final class AddPhysicsModifierCommand implements GameCommand {
    private final Entity target;
    private final PhysicsModifier modifier;

    public AddPhysicsModifierCommand(@NonNull Entity target, @NonNull PhysicsModifier modifier) {
        this.target = Objects.requireNonNull(target);
        this.modifier = Objects.requireNonNull(modifier);
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().addModifier(target.getBody(), modifier);
    }
}