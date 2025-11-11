package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.Weapon;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.weapon.WeaponRegistry;
import org.eclipse.jdt.annotation.NonNull;

import java.util.*;

/**
 * Represents the weapon inventory for a team.
 * <p>
 * This class tracks which weapons are unlocked and manages their
 * corresponding ammunition counts. It allows adding, removing, and
 * checking weapons, as well as firing them when conditions permit.
 * <p>
 * The inventory is typically maintained per team, rather than per worm.
 */
public final class Inventory {
    private static final WeaponRegistry registry = new WeaponRegistry();
    private final Set<WeaponId> unlocked = EnumSet.noneOf(WeaponId.class);
    private final Map<WeaponId, Integer> ammo = new EnumMap<>(WeaponId.class);

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
     * @throws IllegalArgumentException if the weapon type is unknown
     */
    public void unlock(WeaponId id) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        this.unlocked.add(id);
    }

    /**
     * Locks a weapon, removing its availability.
     *
     * @param id the weapon identifier to lock
     */
    public void lock(WeaponId id) {
        this.unlocked.remove(id);
    }

    /**
     * Returns the current ammo count for the specified weapon.
     *
     * @param id the weapon identifier
     * @return the number of remaining ammo units, or {@code 0} if none
     */
    public int getAmmo(WeaponId id) {
        return this.ammo.getOrDefault(id, 0);
    }

    /**
     * Adds a specified amount of ammo for a weapon.
     *
     * @param id     the weapon identifier
     * @param amount the amount of ammo to add (can be negative to reduce ammo)
     * @throws IllegalArgumentException if the weapon type is unknown
     */
    public void addAmmo(WeaponId id, int amount) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        if (amount == 0) return;
        this.ammo.merge(id, amount, Integer::sum);
    }

    /**
     * Checks whether a weapon can currently be fired.
     * <p>
     * A weapon can fire if it is unlocked and has at least one ammo unit left.
     *
     * @param id the weapon identifier
     * @return {@code true} if the weapon can fire, {@code false} otherwise
     * @throws IllegalArgumentException if the weapon type is unknown
     */
    public boolean canFire(WeaponId id) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        return this.isUnlocked(id) && (this.getAmmo(id) > 0);
    }

    /**
     * Attempts to fire the given weapon.
     * <p>
     * If the weapon is unlocked and has ammo available, one ammo unit is consumed
     * and a new {@link Projectile} is created and returned.
     * Otherwise, an {@link IllegalArgumentException} is thrown.
     *
     * @param id     the weapon identifier
     * @param params the firing parameters used to create the projectile (must not be {@code null})
     * @return the newly created {@link Projectile}
     * @throws NullPointerException     if {@code params} is {@code null}
     * @throws IllegalArgumentException if the weapon is unknown or cannot fire
     */
    public Projectile fire(WeaponId id, @NonNull FireParams params) {
        Objects.requireNonNull(params);
        Weapon w = registry.get(id);
        if (w == null) throw new IllegalArgumentException("Unknown weapon : " + id);
        if (this.canFire(id)) {
            this.ammo.merge(id, -1, Integer::sum);
            return w.createProjectile(params);
        }
        throw new IllegalArgumentException("Weapon can't fire : " + id);
    }

    /**
     * Returns an unmodifiable view of the currently unlocked weapons.
     *
     * @return an unmodifiable {@link Set} of unlocked weapon identifiers
     */
    public Set<WeaponId> unlockedWeapons() {
        return Collections.unmodifiableSet(this.unlocked);
    }

    /**
     * Returns an unmodifiable snapshot of the current ammo counts.
     *
     * @return an unmodifiable {@link Map} mapping each {@link WeaponId} to its ammo count
     */
    public Map<WeaponId, Integer> ammoSnapshot() {
        return Collections.unmodifiableMap(this.ammo);
    }
}
