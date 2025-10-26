package com.lllmtb.terrain;

public class Map implements Imap {

    private int width;
    private int height;
    private BLOC[][] mapMatrix;

    public Map(int width, int height) {
        this.mapMatrix = new BLOC[height][width];
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public BLOC[][] getMapMatrix() {
        return this.mapMatrix;
    }

    public void setMap(int x, int y, BLOC bloc) {
            this.mapMatrix[y][x] = bloc; // A priori pas besoin de verif si x,y appartient a la map , les methode appelante appelerons inMap au preablable
    
        }
        

    public boolean isBloc(BLOC bloc, int x, int y) { // Prend des int , dans colision on regarderas lorsque l'item 'change de case' par apport a la map
        return this.mapMatrix[y][x] == bloc; // A priori pas besoin de verif si x,y appartient a la map , collision appeleras inMap avant cette methode
    }

    public boolean inMap(double x, double y) {
        return (0 <= x && 0 <= y && x < this.width && y < this.height);
    }

    @Override
    public String toString() {
        String string = "width : " + this.width + ", height : " + this.height + ", matrice : \n";
        for (int i = 0; i < this.height; i++) {
            string += "\n";
            for (int j = 0; j < this.width; j++) {
                string += this.mapMatrix[i][j];
            }
        }
        return string;
    }
}
