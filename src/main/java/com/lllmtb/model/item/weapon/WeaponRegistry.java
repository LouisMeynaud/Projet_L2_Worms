package com.lllmtb.model.item.weapon;

import java.util.*;

/**
 * Immutable registry that stores canonical {@link Weapon} instances.
 * <p>
 * This registry is typically created once at game startup and provides
 * access to predefined weapon types (e.g., gun, bazooka).
 * <p>
 * Each {@link WeaponId} is associated with a single {@link Weapon} instance.
 */
public final class WeaponRegistry {

    private final Map<WeaponId, Weapon> registry = new EnumMap<>(WeaponId.class);

    /**
     * Creates a new {@code WeaponRegistry} and initializes it
     * with all available weapon instances.
     * <p>
     * This constructor populates the registry with the canonical
     * implementations of each weapon type.
     */
    public WeaponRegistry() {
        this.registry.put(WeaponId.GUN, new GunWeapon());
        this.registry.put(WeaponId.BAZOOKA, new BazookaWeapon());
    }

    /**
     * Retrieves the {@link Weapon} associated with the given {@link WeaponId}.
     *
     * @param id the weapon identifier to look up
     * @return the corresponding {@link Weapon} instance, or {@code null} if not found
     */
    public Weapon get(WeaponId id) {
        return registry.get(id);
    }

    /**
     * Checks whether the registry contains a weapon for the given {@link WeaponId}.
     *
     * @param id the weapon identifier to check
     * @return {@code true} if the registry contains the weapon, {@code false} otherwise
     */
    public boolean contains(WeaponId id) {
        return registry.containsKey(id);
    }
}
