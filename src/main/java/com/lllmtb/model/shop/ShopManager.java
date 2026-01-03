package com.lllmtb.model.shop;

import java.util.ArrayList;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.NotificationEvent;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;

public class ShopManager {


    private final ArrayList<ShopElement> items;
    private final EventBus eventBus;
    /**
     * Constructor initializes the shop manager and populates the shop with items.
     */
    public ShopManager(EventBus eventBus) {
        this.items = new ArrayList<>();
        this.eventBus = eventBus;
        initShop();
    }

    /**
     * Initializes the shop with items, their prices, and ammo pack sizes.
     */
    private void initShop() {
        //prix temporaires , a changer plus tard

        items.add(new ShopElement(WeaponId.SHOTGUN, "shotgun", 120, 40, 5));
        items.add(new ShopElement(WeaponId.BAZOOKA, "bazooka", 150, 50, 1));
        items.add(new ShopElement(WeaponId.GASGUN, "gasgun", 150, 60, 2));
        items.add(new ShopElement(WeaponId.ELECTRONCANON, "electroncanon", 250, 70, 1));
        items.add(new ShopElement(WeaponId.HEALINGBAZOOKA, "healingbazooka", 100, 40, 1));
        items.add(new ShopElement(WeaponId.BOW, "bow", 100, 40, 3));

        //Tools
        items.add(new ShopElement(ToolId.GRENADE, "grenade", 100, 60, 1));
        items.add(new ShopElement(ToolId.JETPACK, "jetpack", 150, 90, 1));
        items.add(new ShopElement(ToolId.CALLSTRIKE, "callstrike", 150, 90, 1));
        items.add(new ShopElement(ToolId.PARACHUTE, "parachute", 50, 20, 1));
        items.add(new ShopElement(ToolId.CATCONCRETE, "catconcrete", 150, 90, 1));
        items.add(new ShopElement(ToolId.TOWER, "tower", 150, 150, 1));
        items.add(new ShopElement(ToolId.MINE, "mine", 100, 100, 1));
        items.add(new ShopElement(ToolId.MEDKIT, "medkit", 75, 75, 1));
        items.add(new ShopElement(ToolId.TRAP, "trap", 100, 100, 1));

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
                String cleImage = "Hand" + id.toString();
                eventBus.postQueued(new NotificationEvent("Achat réussi : " + itemBuy.name, cleImage));
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
        if (itemBuy == null) {
            Logger.logger.warn("Item not found in shop: {}", id);
            return;
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
            String key = "Hand" + id.toString();
            eventBus.postQueued(new NotificationEvent("+ " + itemBuy.ammoPackSize + " ammo for " + itemBuy.name, key));

            Logger.logger.info("Achat ammo reussie : " + itemBuy.name);

        } else {
            Logger.logger.info("Achat rater pas assez d'argent");
        }
    }
}