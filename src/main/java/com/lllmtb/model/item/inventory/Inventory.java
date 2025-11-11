package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.Weapon;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.weapon.WeaponRegistry;
import org.eclipse.jdt.annotation.NonNull;

import java.util.*;

/**
 * Inventory per team. Tracks unlocked weapons and ammo counts.
 */
public final class Inventory {
    private static final WeaponRegistry registry = new WeaponRegistry();
    private final Set<WeaponId> unlocked = EnumSet.noneOf(WeaponId.class);
    private final Map<WeaponId, Integer> ammo = new EnumMap<>(WeaponId.class);


    public boolean isUnlocked(WeaponId id) { return unlocked.contains(id); }


    public void unlock(WeaponId id) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        this.unlocked.add(id);
    }


    public void lock(WeaponId id) { this.unlocked.remove(id); }


    public int getAmmo(WeaponId id) { return this.ammo.getOrDefault(id, 0); }


    public void addAmmo(WeaponId id, int amount) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        if (amount == 0) return;
        this.ammo.merge(id, amount, Integer::sum);
    }


    public boolean canFire(WeaponId id) {
        if (!registry.contains(id)) throw new IllegalArgumentException("Unknown weapon: " + id);
        return this.isUnlocked(id) && (this.getAmmo(id) > 0);
    }


    /**
     * Attempt to fire the weapon. If successful returns the created Projectile.
     * If not allowed (locked / no ammo) returns null.
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


    // helpers for serialization etc.
    public Set<WeaponId> unlockedWeapons() { return Collections.unmodifiableSet(this.unlocked); }
    public Map<WeaponId, Integer> ammoSnapshot() { return Collections.unmodifiableMap(this.ammo); }
}