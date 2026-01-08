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


/**
 * Represents a drop loot.
 * A {@code Drop} falls from the sky, can touch the ground, and can be collected by a worm.
 * When collected, it can unlock a weapon or give tool ammo to the team.
 */
public class Drop extends Entity {
    private final PhysicsBody body;
    public final transient ItemId itemIdToUnlock;
    private static final double DROP_MAX_FALL_SPEED = -9.0;
    private final CollisionType collisionType = CollisionType.TOUCH;
    private boolean onGround = false;
    private final String itemCategory; // "WEAPONID" ou "TOOLID"
    private final String itemName;

    /**
     * Constructor of the Drop, it creates a new drop.
     * @param position the start position of the drop on the map.
     * @param itemId the item (weapon or tool) linked to this drop.
     */
    public Drop(Position position, ItemId itemId) {
        this.itemIdToUnlock = itemId;
        this.body = new PhysicsBody(this, position, new Vector(0, 0), 0.1, 2, 2);
        this.itemName = ((Enum<?>) itemId).name();
        this.itemCategory = (itemId instanceof WeaponId) ? "WEAPONID" : "TOOLID";
    }

    /**
     * @return the collision type of the drop
     */
    @Override
    public CollisionType getCollisionType() {
        return this.collisionType;
    }

    /**
     * @return the {@link PhysicsBody} of this drop
     */
    @Override
    public PhysicsBody getBody() {
        return this.body;
    }

    /**
     * Registers the drop in the game world and adds physics rules to make the drop fall down with a speed limit.
     * @return a list of {@link GameCommand} to apply in the world.
     */
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

    /**
     * Called when the drop hits another entity.
     * If a worm collects it, the team gets:
     * <ul>
     *   <li>A new weapon unlocked or tool ammo added</li>
     *   <li>A random amount of ammo (1 to 3)</li>
     *   <li>The drop is removed from the world</li>
     *   <li>A notification is sent to the UI</li>
     * </ul>
     * If a projectile hits it, the drop is simply removed.
     * @param hitEntity the entity that touched or hit the drop
     * @return a {@link GameCommand} to run after impact
     */
    public GameCommand onImpact(Entity hitEntity) {
        GameCommand commands = null;
        if (hitEntity instanceof Worm worm) {
            Random random = new Random();
            Team team = worm.getTeam();
            int ammoAmount = random.nextInt(1, 4);
            if (this.itemIdToUnlock instanceof ToolId toolID) {
                team.addAmmo(toolID, ammoAmount);
                commands = world -> {
                    new RemoveEntityCommand(this).execute(world);
                    world.getBus().postQueued(new NotificationEvent("+ " + ammoAmount + " munitions", toolID.toString()));
                };

                Logger.logger.debug("Drop pick up, tool obtained : {} , numbers of use : {} !", this.itemIdToUnlock, ammoAmount);
            } else if (this.itemIdToUnlock instanceof WeaponId weaponID) {
                team.unlockWeapon(weaponID);
                team.addAmmo(weaponID, ammoAmount);
                commands = world -> {
                    new RemoveEntityCommand(this).execute(world);
                    world.getBus().postQueued(new NotificationEvent("Arme " + weaponID + " obtenue !", weaponID.toString()));
                    world.getBus().postQueued(new NotificationEvent("+ " + ammoAmount + " munitions pour " + weaponID, weaponID.toString()));
                };
                Logger.logger.debug("Drop pick up, weapon obtained : {} , ammo : {} !", this.itemIdToUnlock, ammoAmount);
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
     * set the onGround state of the drop
     * @param onGround whether the drop is on the ground
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * @return "WeaponId" or "ToolId"
     */
    public String getItemCategory() {
        return itemCategory;
    }
    /**
     * @return the item's name
     */
    public String getItemName() {
        return itemName;
    }
}

