package com.lllmtb;

public interface IItems {

    boolean inInventory(); //return true if the item is in the player's iventory

    boolean useable(); //return true if the player can use the item (in his inventory and if he has at least one ammunition)

    void oneUse(); //simulate one use of the item by reducing its ammunitions by one if the player can use it
}
