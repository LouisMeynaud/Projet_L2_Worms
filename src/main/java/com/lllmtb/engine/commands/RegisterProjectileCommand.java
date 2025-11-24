package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.physics.ProjectileRegisteredEvent;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.projectile.Projectile;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public final class RegisterProjectileCommand implements GameCommand {
    private final Projectile projectile;
    private final Entity owner;

    public RegisterProjectileCommand(@NonNull Projectile projectile, Entity owner) {
        this.projectile = Objects.requireNonNull(projectile);
        this.owner = owner;
    }

    @Override
    public void execute(GameWorld world) {
        world.getPhysics().add(projectile.getBody());
        world.getBus().postQueued(new ProjectileRegisteredEvent(projectile, owner));
    }
}