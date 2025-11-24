package com.lllmtb.model.item;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Immutable registry that stores canonical {@link I} instances.
 * <p>
 * This registry is created once at game startup and provides
 * access to predefined item types.
 * <p>
 * Each {@link T} is associated with a single {@link I} instance.
 */
public abstract class ItemRegistry<T extends Enum<T> & ItemId, I extends Item> {

    protected final EnumMap<T, I> registry;
    protected final EnumMap<T, Integer> ammo;

    /**
     * Creates a new {@code ItemRegistry} and initializes it
     * with all available item instances.
     * <p>
     * This constructor populates the registry with the canonical
     * implementations of each item type.
     */
    public ItemRegistry(Class<T> idClass) {
        this.registry = new EnumMap<>(idClass);
        this.ammo = new EnumMap<>(idClass);
    }

    /**
     * Retrieves the {@link I} associated with the given {@link T}.
     *
     * @param id the item identifier to look up
     * @return the corresponding {@link I} instance, or {@code null} if not found
     */
    public I get(T id) {
        return registry.get(id);
    }

    /**
     * Checks whether the registry contains an item for the given {@link T}.
     *
     * @param id the item identifier to check
     * @return {@code true} if the registry contains the item, {@code false} otherwise
     */
    public boolean contains(T id) {
        return registry.containsKey(id);
    }

    /**
     * Returns an unmodifiable snapshot of the current ammo counts.
     *
     * @return an unmodifiable {@link Map} mapping each {@link T} to its ammo count
     */
    public Map<T, Integer> ammoSnapshot() {
        return Collections.unmodifiableMap(ammo);
    }

    /**
     * Returns the current ammo count for the specified item.
     *
     * @param id the item identifier
     * @return the number of remaining ammo units, or {@code 0} if none
     */
    public int getAmmo(T id) {
        return ammo.getOrDefault(id, 0);
    }

    /**
     * Adds a specified amount of ammo for an item.
     *
     * @param id     the item identifier
     * @param amount the amount of ammo to add (can be negative to reduce ammo)
     * @throws IllegalArgumentException if the item type is unknown
     */
    public void addAmmo(T id, int amount) {
        if (!contains(id)) throw new UnknownItemException(id);
        if (amount == 0) return;
        this.ammo.merge(id, amount, Integer::sum);
    }
}
