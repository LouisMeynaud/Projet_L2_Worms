package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.physics.EntityRemovedEvent;
import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * A command that removes an entity from the game world.
 */
public final class RemoveEntityCommand implements GameCommand {
    private final Entity entity;

    public RemoveEntityCommand(@NonNull Entity entity) {
        this.entity = Objects.requireNonNull(entity);
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().remove(entity.getBody());
        world.getBus().postQueued(new EntityRemovedEvent(entity));
    }
}