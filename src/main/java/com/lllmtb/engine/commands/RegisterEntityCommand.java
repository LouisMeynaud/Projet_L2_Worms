package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.physics.EntityRegisteredEvent;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * A command that registers an entity within the game world.
 */
public final class RegisterEntityCommand implements GameCommand {
    private final Entity entity;
    private final Entity owner;

    public RegisterEntityCommand(@NonNull Entity entity, Entity owner) {
        this.entity = Objects.requireNonNull(entity);
        this.owner = owner;
    }

    public RegisterEntityCommand(@NonNull Entity entity) {
        this(entity, null);
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().add(entity.getBody());
        world.getBus().postQueued(new EntityRegisteredEvent(entity, owner));
    }
}