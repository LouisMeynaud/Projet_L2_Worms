package com.lllmtb.terrain;
import com.lllmtb.physics.Position;

/**
 * Représente les cartes du jeu sous forme d'une matrice de TILESs,
 * représentant les différents éléments présents à une position x y.
 * Une case de la matrice est représentée par une coordonnée x y entière (0,0)
 * en bas à gauche.
 */

public interface IMap {
    /**
     * Enum représentant les différents éléments de la carte. Si un élément est
     * ajouté,pensez à l'ajouter au switch de la méthode CreateMatrix de MapLoader.
     */

    /** getteur ordinair */
    public int getWidth();
    public int getHeight();
    public Tile.TILES[][] getMapMatrix();

    /**
     * Setteur modifiant une case de la matrice.
     * 
     * @param x    la coordonée x en int
     * @param y    la coordonée y en int
     * @param TILES , le TILES par lequel la base sera changer
     */
    public void setMap(Position position, Tile.TILES tile);

    /**
     * @param position
     * @return une nouvelle position correspondant a la case de la matrice 
     * (car dans le jeu 0,0 est en bas a gauche or dans la matrix (0,0) est en haut a gauche)
     */
    public Position cooMapToCooMatrix(Position position);
    /**
     * 
     * @param TILES Le TILES dont on verif la presence.
     * @param position la position a verif
     */
    public boolean isTILES(Tile.TILES tile,Position position);

    /**
     * verif que la coordonée x,y est bien dans la map (la matrix)
     */
    public boolean inMap(double x, double y);

    /** un toString ordinaire servant au test primaire */
    public String toString();
}
