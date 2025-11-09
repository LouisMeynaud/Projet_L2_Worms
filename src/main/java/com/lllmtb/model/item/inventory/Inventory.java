package com.lllmtb.model.item.inventory;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;
import com.lllmtb.model.item.tool.Tool;
import com.lllmtb.model.item.weapon.Weapon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Inventory {

    private Set<Weapon> weaponsUnlocked = new HashSet<>();
    private Map<Class<? extends IItems>, Integer> ammos = new HashMap<>();

    public Inventory(Set<Weapon> weaponsUnlocked, Map<Class<? extends IItems>, Integer> ammos) {
        this.weaponsUnlocked = weaponsUnlocked;
        this.ammos = ammos;
    }

    public Set<Weapon> getWeaponsUnlocked() {
        return weaponsUnlocked;
    }

    public Map<Class<? extends IItems>, Integer> getAmmos() {
        return ammos;
    }

    public boolean useable(Weapon w) {
        if (this.weaponsUnlocked.contains(w)) {
            return this.ammos.get(w.getClass()) > 0;
        }
        return false;
    }

    public boolean useable(Tool t) {
        if (this.ammos.containsKey(t.getClass())) {
            return this.ammos.get(t.getClass()) > 0;
        }
        return false;
    }

    public void addAmmo(IItems i, Integer n) {
        if (this.ammos.containsKey(i.getClass())) {
            this.ammos.replace(i.getClass(), this.ammos.get(i.getClass()) + n);
        } else {
            this.ammos.put(i.getClass(), n);
        }
    }

    public void addItem(Tool t, Integer n) {
        this.ammos.putIfAbsent(t.getClass(), n);
    }

    public void addItem(Weapon w, Integer n) {
        if (this.weaponsUnlocked.contains(w)) {
            this.ammos.replace(w.getClass(), this.ammos.get(w.getClass()) + n);
        } else {
            this.weaponsUnlocked.add(w);
            this.ammos.put(w.getClass(), n);
        }
    }

    public void use(IItems i, Entity e, double angle) {
        //on lance ici les methodes dans GameLoop pour simuler l'utilisation de l'item
    }
}
