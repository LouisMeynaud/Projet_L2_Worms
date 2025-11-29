package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.physics.PhysicsModifierAddedEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsModifier;

import java.util.Objects;

public final class AddPhysicsModifierCommand implements GameCommand {
    private final Entity target;
    private final PhysicsModifier modifier;

    public AddPhysicsModifierCommand(Entity target, PhysicsModifier modifier) {
        this.target = Objects.requireNonNull(target);
        this.modifier = Objects.requireNonNull(modifier);
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().addModifier(target.getBody(), modifier);
    }
}