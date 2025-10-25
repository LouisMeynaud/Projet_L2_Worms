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
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bloc[][] getMapMatrix() {
        return mapMatrix;
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
