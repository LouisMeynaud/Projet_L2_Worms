package com.lllmtb.terrain.map;
    /**
     * Enum represent the different elements of the map. 
     * If an element is added, remember to add it to the switch statement of the MapLoader's CreateMatrix method.
     */

public enum Tile {
    DIRT, WATER, AIR , CAVERN, GRASS, UNKNOWN
}//UNKNOW uniquement utile pour les test , ne sera jamais initialisé dans une matrice.
