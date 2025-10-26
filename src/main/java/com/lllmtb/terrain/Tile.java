package com.lllmtb.terrain;

public class Tile {
     /**
     * Enum représentant les différents éléments de la carte. Si un élément est
     * ajouté,pensez à l'ajouter au switch de la méthode CreateMatrix de MapLoader.
     */
     public static enum TILES {
        DIRT, WATER, AIR
    };
}
