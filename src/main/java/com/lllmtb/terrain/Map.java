package com.lllmtb.terrain;

import java.io.IOException;

public class Map {
    public enum Bloc {
        DIRT, WATER, AIR
    };

    private int width;
    private int height;
    private Bloc[][] mapMatrix;

    public Map(int width, int height) throws IOException {
        this.mapMatrix = new Bloc[height][width];
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Bloc[][] getMapMatrix() {
        return this.mapMatrix;
    }

    public void setMap(int i,int j,Bloc bloc){
        if(i<this.height && j<this.width){
        this.mapMatrix[i][j] = bloc;}
    }

    public boolean inMap(int x,int y){
        return (0<= x && 0 <= y && x< this.width && y<this.height);
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
