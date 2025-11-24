package com.lllmtb.model.item.inventory;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.item.tool.*;
import com.lllmtb.model.item.weapon.*;
import org.eclipse.jdt.annotation.NonNull;

import java.util.*;

/**
 * Represents the weapon inventory for a team.
 * <p>
 * This class tracks which weapons are unlocked and manages their
 * corresponding ammunition counts. It allows locking and unlocking weapons,
 * as well as firing them when conditions permit.
 * This class also manages tools.
 * <p>
 * The inventory is maintained per team, rather than per worm.
 */
public final class Inventory {
    public final WeaponRegistry weapons = new WeaponRegistry();
    private final Set<WeaponId> unlocked = EnumSet.noneOf(WeaponId.class);
    public final ToolRegistry tools = new ToolRegistry();

    /**
     * Checks if the given weapon is currently unlocked.
     *
     * @param id the weapon identifier
     * @return {@code true} if the weapon is unlocked, {@code false} otherwise
     */
    public boolean isUnlocked(WeaponId id) {
        return unlocked.contains(id);
    }

    /**
     * Unlocks a weapon, making it available for use.
     *
     * @param id the weapon identifier to unlock
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public void unlock(WeaponId id) {
        if (!weapons.contains(id)) throw new UnknownWeaponException(id);
        unlocked.add(id);
    }

    /**
     * Locks a weapon, removing its availability.
     *
     * @param id the weapon identifier to lock
     */
    public void lock(WeaponId id) {
        unlocked.remove(id);
    }

    /**
     * Returns an unmodifiable view of the currently unlocked weapons.
     *
     * @return an unmodifiable {@link Set} of unlocked weapon identifiers
     */
    public Set<WeaponId> unlockedWeapons() {
        return Collections.unmodifiableSet(unlocked);
    }

    /**
     * Checks whether a weapon can currently be fired.
     * <p>
     * A weapon can fire if it is unlocked and has at least one ammo unit left.
     *
     * @param id the weapon identifier
     * @return {@code true} if the weapon can fire, {@code false} otherwise
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public boolean canFire(WeaponId id) {
        if (!weapons.contains(id)) throw new UnknownWeaponException(id);
        return isUnlocked(id) && (weapons.getAmmo(id) > 0);
    }

    /**
     * Attempts to fire the given weapon.
     * <p>
     * If the weapon is unlocked and has ammo available, one ammo unit is consumed
     * and a new {@link Projectile} is created and returned.
     *
     * @param id     the weapon identifier
     * @param params the firing parameters used to create the projectile (must not be {@code null})
     * @return the newly created {@link Projectile}
     * @throws NullPointerException     if {@code params} is {@code null}
     * @throws UnknownWeaponException if the weapon is unknown
     * @throws UnusableWeaponException if the weapon cannot fire
     */
    public List<GameCommand> fire(WeaponId id, @NonNull FireParams params) {
        Objects.requireNonNull(params);
        Weapon w = weapons.get(id);
        if (w == null) throw new UnknownWeaponException(id);
        if (canFire(id)) {
            weapons.addAmmo(id, -1);
            return w.fire(params);
        }
        throw new UnusableWeaponException(id, params.owner);
    }

    /**
     * Checks whether a tool can be used.
     * <p>
     * A tool can be used if it has at least one ammo unit left (more conditions later?).
     *
     * @param id the tool identifier
     * @return {@code true} if the tool can be used, {@code false} otherwise
     * @throws UnknownToolException if the tool type is unknown
     */
    public boolean canUse(ToolId id) {
        if (!tools.contains(id)) throw new UnknownToolException(id);
        return tools.getAmmo(id) > 0;
    }

    /**
     * Attempts to use the given tool.
     * <p>
     * If the tool has ammo available, one ammo unit is consumed
     * and a new list of instructions is created and returned.
     *
     * @param id     the tool identifier
     * @param params the use parameters (must not be {@code null})
     * @return the newly created {@link List<GameCommand>} commands to execute
     * @throws NullPointerException     if {@code params} is {@code null}
     * @throws UnknownToolException if the tool is unknown
     * @throws UnusableToolException if the tool is cannot be used
     */
    public List<GameCommand> use(ToolId id, @NonNull UseParams params) {
        Objects.requireNonNull(params);
        Tool t = tools.get(id);
        if (t == null) throw new UnknownToolException(id);
        if (canUse(id)) {
            tools.addAmmo(id, -1);
            return t.use(params);
        }
        throw new UnusableToolException(id, params.owner);
    }
}
