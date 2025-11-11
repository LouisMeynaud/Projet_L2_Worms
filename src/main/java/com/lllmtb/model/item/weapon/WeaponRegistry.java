package com.lllmtb.model.item.weapon;

import java.util.*;

/**
 * Immutable registry of canonical Weapon instances. Create at game startup.
 */
public final class WeaponRegistry {
    private final Map<WeaponId, Weapon> registry = new EnumMap<>(WeaponId.class);

    public WeaponRegistry() {
        this.registry.put(WeaponId.GUN, new GunWeapon());
        this.registry.put(WeaponId.BAZOOKA, new BazookaWeapon());
    }

    public Weapon get(WeaponId id) { return registry.get(id); }
    public boolean contains(WeaponId id) { return registry.containsKey(id); }
}