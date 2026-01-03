package com.lllmtb.model.entity;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.engine.commands.RemoveEntityCommand;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.ui.graphical.NotificationEvent;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Transform;
import com.lllmtb.physics.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Drop extends Entity {
    private PhysicsBody body;
    public final ItemId itemIdToUnlock;
    private static final double DROP_MAX_FALL_SPEED = -9.0;
    private CollisionType collisionType = CollisionType.TOUCH;
    private boolean onGround = false;

    public Drop(Position position, ItemId itemId) {
        this.itemIdToUnlock = itemId;
        this.body = new PhysicsBody(this, position, new Vector(0, 0), 0.1, 2, 2);
    }

    @Override
    public CollisionType getCollisionType() {
        return this.collisionType;
    }

    @Override
    public PhysicsBody getBody() {
        return this.body;
    }

    public List<GameCommand> addModifier() {
        List<GameCommand> commands = new ArrayList<>();
        commands.add(new RegisterEntityCommand(this));

        Transform transform = (v) -> {
            double vy = v.getY();
            if (vy < 0) vy = Math.max(vy, DROP_MAX_FALL_SPEED);
            return new Vector(v.getX(), vy);
        };
        PhysicsModifier modifier = new PhysicsModifier(transform, null, Double.POSITIVE_INFINITY, true);
        commands.add(new AddPhysicsModifierCommand(this, modifier));

        return commands;
    }

    public GameCommand onImpact(Entity hitEntity) {
        GameCommand commands = null;
        if (hitEntity instanceof Worm worm) {
            Random random = new Random();
            Team team = worm.getTeam();
            int ammoAmount = random.nextInt(1, 6);
            if (this.itemIdToUnlock instanceof ToolId toolID) {
                team.addAmmo(toolID, ammoAmount);
                commands = world -> {
                    new RemoveEntityCommand(this).execute(world);
                    world.getBus().postQueued(new NotificationEvent("+ " + ammoAmount + " ammo", toolID.toString()));
                };

                Logger.logger.debug("Drop ramasse, outil obtenu : {} , nombre d'utilisation : {} !", this.itemIdToUnlock, ammoAmount);
            } else if (this.itemIdToUnlock instanceof WeaponId weaponID) {
                team.unlockWeapon(weaponID);
                team.addAmmo(weaponID, ammoAmount);
                commands = world -> {
                    new RemoveEntityCommand(this).execute(world);
                    world.getBus().postQueued(new NotificationEvent("Weapon " + weaponID + " obtained !", weaponID.toString()));
                                        world.getBus().postQueued(new NotificationEvent("+ " + ammoAmount + " ammo for " + weaponID, weaponID.toString()));
                };
                Logger.logger.debug("Drop ramasse, arme obtenu : {} , nombre d'utilisation : {} !", this.itemIdToUnlock, ammoAmount);
            }
        } else if (hitEntity instanceof Projectile) {
            commands = new RemoveEntityCommand(this);
        }
        return commands;
    }

    /**
     * return if the drop is on the ground
     * @return {@code true} if the drop is on the ground, {@code false} otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * set the drop on the ground
     * @return the {@link GameCommand} with value {@code null}
     */
    public GameCommand setOnGround() {
        this.onGround = true;
        return null;
    }
}

