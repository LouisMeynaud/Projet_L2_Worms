package com.lllmtb.model.item;

public interface ItemId {
    /**
     * @return the shoot mode of the weapon/tools
     */
    ShootMode getShootMode();

    /**
     * @return the info string of the item
     */
    String getInfo();

    /**
     * @return the maximum time to charge the weapon/tools
     */
    int getMaxChargeTime();
}
