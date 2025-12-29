package com.lllmtb.model.item;

public interface ItemId {
    /**
     * Returns whether this item uses a crosshair target instead of directional aiming.
     * @return true if this item should display a crosshair
     */
    default boolean usesCrosshair() {
        return false;
    }
}
