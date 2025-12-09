package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.Item;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;

/**
 * Immutable descriptor + factory for creating a single projectile when fired.
 */
public abstract class Weapon implements Item {
    public abstract WeaponId id();
    // + metadata (sprites, audio...)

    /**
     * Create commands for this weapon firing action.
     * If firing is not allowed for other reasons this should still return commands
     * or higher-level Inventory.fire() should have prevented the call.
     */
    public abstract List<GameCommand> fire(@NonNull UseParams params);
}