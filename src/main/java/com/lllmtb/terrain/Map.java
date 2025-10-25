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
}
