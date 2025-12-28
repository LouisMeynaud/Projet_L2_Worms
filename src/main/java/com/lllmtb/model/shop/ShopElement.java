package com.lllmtb.model.shop;

import com.lllmtb.model.item.ItemId;

public class ShopElement {
        public final ItemId id;
        public final String name;
        public final int unlockPrice;
        public final int ammoPrice;
        public final int ammoPackSize;

        public ShopElement(ItemId id, String name, int unlockPrice, int ammoPrice, int ammoPackSize) {
            this.id = id;
            this.name = name;
            this.unlockPrice = unlockPrice;
            this.ammoPrice = ammoPrice;
            this.ammoPackSize = ammoPackSize;
        }
    }