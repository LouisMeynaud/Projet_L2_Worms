package com.lllmtb.model.shop;

import java.util.ArrayList;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;

public class ShopManager {


    private final ArrayList<ShopElement> items;

    public ShopManager() {
        this.items = new ArrayList<>();
        initShop();
    }

    private void initShop() {
        //prix temporaires , a changer plus tard
        items.add(new ShopElement(WeaponId.GUN, "GUN", 50, 20, 5));
        items.add(new ShopElement(WeaponId.SHOTGUN, "SHOTGUN", 120, 40, 5));
        items.add(new ShopElement(WeaponId.BAZOOKA, "BAZOOKA", 150, 50, 1));
        items.add(new ShopElement(WeaponId.GASGUN, "GASGUN", 200, 60, 2));
        items.add(new ShopElement(WeaponId.ELECTRONCANON, "ELECTRONCANON", 250, 70, 1));
        items.add(new ShopElement(WeaponId.HEALINGBAZOOKA, "HEALINGBAZOOKA", 100, 40, 1));

        items.add(new ShopElement(ToolId.PARACHUTE, "Parachute", 50, 20, 1));
    }

    public ArrayList<ShopElement> getShopElements() {
        return items;
    }

    public void buyItem(ItemId id, Team team) {
        ShopElement itemBuy = null;
        for (ShopElement element : items) {
            if (element.id == id) {
                itemBuy = element;
                break;
            }
        }
        if (team.getCredits() >= itemBuy.unlockPrice) {
                team.removeCredits(itemBuy.unlockPrice);
                team.unlockWeapon((WeaponId) id);
                Logger.logger.info("Achat arme reussie : " + itemBuy.name);
        } else {
            Logger.logger.info("Achat rater pas assez d'argent");
        }
    }

    public void buyAmmo(ItemId id, Team team) {
        ShopElement itemBuy = null;
        for (ShopElement element : items) {
            if (element.id == id) {
                itemBuy = element;
                break;
            }
        }
        if (id instanceof WeaponId) {
            if (!team.getInventory().isUnlocked((WeaponId) id)) {
                Logger.logger.info("impossible arme non Unlock");
                return;
            }
        }
        if (team.getCredits() >= itemBuy.ammoPrice) {
            team.removeCredits(itemBuy.ammoPrice);

            if (id instanceof WeaponId) {
                team.addAmmo((WeaponId) id, itemBuy.ammoPackSize);
            } else if (id instanceof ToolId) {
                team.addAmmo((ToolId) id, itemBuy.ammoPackSize);
            }
            
            Logger.logger.info("Achat ammo reussie : " + itemBuy.name);

        } else {
            Logger.logger.info("Achat rater pas assez d'argent");
        }
    }
}