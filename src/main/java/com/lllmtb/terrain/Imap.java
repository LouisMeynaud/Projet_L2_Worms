package com.lllmtb.terrain;

/**
 * Représente les cartes du jeu sous forme d'une matrice de blocs,
 * représentant les différents éléments présents à une position x y.
 * Une case de la matrice est représentée par une coordonnée x y entière (0,0)
 * en bas à gauche.
 */

public interface Imap {
    /**
     * Enum représentant les différents éléments de la carte. Si un élément est
     * ajouté,pensez à l'ajouter au switch de la méthode CreateMatrix de MapLoader.
     */

    public static enum BLOC {
        DIRT, WATER, AIR
    };

    /** getteur ordinair */
    public int getWidth();
    public int getHeight();
    public BLOC[][] getMapMatrix();

    /**
     * Setteur modifiant une case de la matrice.
     * 
     * @param x    la coordonée x en int
     * @param y    la coordonée y en int
     * @param bloc , le bloc par lequel la base sera changer
     */
    public void setMap(int x, int y, BLOC bloc);

    /**
     * 
     * @param bloc Le bloc dont on verif la presence.
     * @param x    La coordonée x en int.
     * @param y    La coordonée y en int.
     */
    public boolean isBloc(BLOC bloc, int x, int y);

    /**
     * 
     * @param x la coordonée x en int.
     * @param y la coordonée y en int.
     */
    public boolean inMap(double x, double y);

    /** un toString ordinaire servant au test primaire */
    public String toString();
}
