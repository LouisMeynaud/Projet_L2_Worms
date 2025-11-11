package com.lllmtb.model.item.weapon;

import java.util.*;

/**
 * Immutable registry of canonical Weapon instances. Create at game startup.
 */
public final class WeaponRegistry {
    private final Map<WeaponId, Weapon> registry;

    public WeaponRegistry(Collection<Weapon> weapons) {
        Map<WeaponId, Weapon> tmp = new EnumMap<>(WeaponId.class);
        for (Weapon w : weapons) {
            if (tmp.put(w.id(), w) != null) {
                throw new IllegalArgumentException("Duplicate weapon id: " + w.id());
            }
        }
        this.registry = Collections.unmodifiableMap(tmp);
    }

    public Weapon get(WeaponId id) { return registry.get(id); }
    public boolean contains(WeaponId id) { return registry.containsKey(id); }
    public Collection<Weapon> all() { return registry.values(); }
}