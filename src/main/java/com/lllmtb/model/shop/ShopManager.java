package com.lllmtb.model.shop;

import java.util.ArrayList;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;

public class ShopManager {


    private final ArrayList<ShopElement> items;

    /**
     * Constructor initializes the shop manager and populates the shop with items.
     */
    public ShopManager() {
        this.items = new ArrayList<>();
        initShop();
    }

    /**
     * Initializes the shop with items, their prices, and ammo pack sizes.
     */
    private void initShop() {
        //prix temporaires , a changer plus tard

        items.add(new ShopElement(WeaponId.SHOTGUN, "SHOTGUN", 120, 40, 5));
        items.add(new ShopElement(WeaponId.BAZOOKA, "BAZOOKA", 150, 50, 1));
        items.add(new ShopElement(WeaponId.GASGUN, "GASGUN", 150, 60, 2));
        items.add(new ShopElement(WeaponId.ELECTRONCANON, "ELECTRONCANON", 250, 70, 1));
        items.add(new ShopElement(WeaponId.HEALINGBAZOOKA, "HEALINGBAZOOKA", 100, 40, 1));
        items.add(new ShopElement(WeaponId.BOW, "BOW", 100, 40, 3));
        items.add(new ShopElement(WeaponId.GRENADE, "GRENADE", 100, 60, 1));

        //Tools
        items.add(new ShopElement(ToolId.JETPACK, "JETPACK", 150, 90, 1));
        items.add(new ShopElement(ToolId.CALLSTRIKE, "CALLSTRIKE", 150, 90, 1));
        items.add(new ShopElement(ToolId.PARACHUTE, "PARACHUTE", 50, 20, 1));
        items.add(new ShopElement(ToolId.CATCONCRETE, "CATCONCRETE", 150, 90, 1));
        items.add(new ShopElement(ToolId.TOWER, "TOWER", 150, 150, 1));
        items.add(new ShopElement(ToolId.MINE, "MINE", 100, 100, 1));
        items.add(new ShopElement(ToolId.MEDKIT, "MEDKIT", 75, 75, 1));

    }

    /**
     * Gets the list of shop elements.
     * @return List of ShopElement objects in the shop.
     */
    public ArrayList<ShopElement> getShopElements() {
        return items;
    }

    /**
     * Buys an item for the given team if they have enough credits.
     * @param id ItemId of the item to be purchased.
     * @param team Team attempting to purchase the item.
     */
    public void buyItem(ItemId id, Team team) {
        ShopElement itemBuy = null;
        for (ShopElement element : items) {
            if (element.id == id) {
                itemBuy = element;
                break;
            }
        }
        if (team.getCredits() >= itemBuy.unlockPrice || team.getCredits() == -1) {
                team.removeCredits(itemBuy.unlockPrice);
                team.unlockWeapon((WeaponId) id);
                Logger.logger.info("Achat arme reussie : " + itemBuy.name);
        } else {
            Logger.logger.info("Achat rater pas assez d'argent");
        }
    }

    /**
     * Buys ammo for the given item for the team if they have enough credits.
     * @param id ItemId of the item to buy ammo for.
     * @param team Team attempting to purchase the ammo.
     */
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
        if (team.getCredits() >= itemBuy.ammoPrice || team.getCredits() == -1) {
            team.removeCredits(itemBuy.ammoPrice);

            if (id instanceof WeaponId) {
                team.addAmmo((WeaponId) id, itemBuy.ammoPackSize);
            } else {
                team.addAmmo((ToolId) id, itemBuy.ammoPackSize);
            }

            Logger.logger.info("Achat ammo reussie : " + itemBuy.name);

        } else {
            Logger.logger.info("Achat rater pas assez d'argent");
        }
    }
}