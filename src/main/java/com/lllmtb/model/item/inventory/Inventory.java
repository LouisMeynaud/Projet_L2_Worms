package com.lllmtb.model.item.inventory;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.UseParams;
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
    // Registry of all weapons with their ammo counts when the team find them
    public final WeaponRegistry weapons = new WeaponRegistry();
    // Set of currently unlocked weapons
    private final Set<WeaponId> unlocked = EnumSet.noneOf(WeaponId.class);
    // Registry of all tools with their ammo counts when the team find them
    public final ToolRegistry tools = new ToolRegistry();

    /**
     * Constructs a new {@code Inventory} with default weapons and tools.
     * <p>
     * By default, the inventory includes:
     * <ul>
     *     <li>A gun with infinite ammo</li>
     *     <li>A baseball bat with infinite ammo</li>
     *     <li>A parachute tool with 2 uses</li>
     * </ul>
     */
    public Inventory() {
        weapons.addAmmo(WeaponId.GUN, -1);
        unlocked.add(WeaponId.GUN);
        weapons.addAmmo(WeaponId.BASEBALLBAT, -1);
        unlocked.add(WeaponId.BASEBALLBAT);
        tools.addAmmo(ToolId.PARACHUTE, 2);
    }

    /**
     * Checks if the given weapon is currently unlocked.
     * @param id the weapon identifier
     * @return {@code true} if the weapon is unlocked, {@code false} otherwise
     */
    public boolean isUnlocked(WeaponId id) {
        return unlocked.contains(id);
    }

    /**
     * Unlocks a weapon, making it available for use.
     * @param id the weapon identifier to unlock
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public void unlock(@NonNull WeaponId id) {
        if (!weapons.contains(id)) throw new UnknownWeaponException(id);
        unlocked.add(Objects.requireNonNull(id));
    }

    /**
     * Locks a weapon, removing its availability.
     * @param id the weapon identifier to lock
     */
    public void lock(@NonNull WeaponId id) {
        unlocked.remove(Objects.requireNonNull(id));
    }

    /**
     * Returns an unmodifiable view of the currently unlocked weapons.
     * @return an unmodifiable {@link Set} of unlocked weapon identifiers
     */
    public Set<WeaponId> unlockedWeapons() {
        return Collections.unmodifiableSet(unlocked);
    }

    /**
     * Checks whether a weapon can currently be fired.
     * A weapon can fire if it is unlocked and has at least one ammo unit left.
     * @param id the weapon identifier
     * @return {@code true} if the weapon can fire, {@code false} otherwise
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public boolean canFire(@NonNull WeaponId id) {
        if (!weapons.contains(id)) throw new UnknownWeaponException(id);
        return isUnlocked(id) && (weapons.getAmmo(id) != 0);
    }

    /**
     * Attempts to fire the given weapon.
     * If the weapon is unlocked and has ammo available, one ammo unit is consumed
     * and a new list of instructions is created and returned.
     * @param id the weapon identifier
     * @param params the firing parameters (must not be {@code null})
     * @return the newly created {@link List<GameCommand>} commands to execute
     * @throws NullPointerException    if {@code params} is {@code null}
     * @throws UnknownWeaponException  if the weapon is unknown
     * @throws UnusableWeaponException if the weapon cannot fire
     */
    public List<GameCommand> fire(@NonNull WeaponId id, @NonNull UseParams params) {
        Objects.requireNonNull(params);
        Weapon w = weapons.get(id);
        if (w == null) throw new UnknownWeaponException(id);
        if (canFire(id)) {
            weapons.addAmmo(id, -1);
            return w.fire(params);
        }
        throw new UnusableWeaponException(id, params.owner.getTeam());
    }

    /**
     * Checks whether a tool can be used.
     * A tool can be used if it has at least one ammo unit left (more conditions later?).
     * @param id the tool identifier
     * @return {@code true} if the tool can be used, {@code false} otherwise
     * @throws UnknownToolException if the tool type is unknown
     */
    public boolean canUse(@NonNull ToolId id) {
        if (!tools.contains(id)) throw new UnknownToolException(id);
        return tools.getAmmo(id) != 0;
    }

    /**
     * Attempts to use the given tool.
     * If the tool has ammo available, one ammo unit is consumed
     * and a new list of instructions is created and returned.
     * @param id the tool identifier
     * @param params the use parameters (must not be {@code null})
     * @return the newly created {@link List<GameCommand>} commands to execute
     * @throws NullPointerException  if {@code params} is {@code null}
     * @throws UnknownToolException  if the tool is unknown
     * @throws UnusableToolException if the tool is cannot be used
     */
    public List<GameCommand> use(@NonNull ToolId id, @NonNull UseParams params) {
        Objects.requireNonNull(params);
        Tool t = tools.get(id);
        if (t == null) throw new UnknownToolException(id);
        if (canUse(id)) {
            tools.addAmmo(id, -1);
            return t.use(params);
        }
        throw new UnusableToolException(id, params.owner.getTeam());
    }
}
