package com.lllmtb.terrain.generation;

public class ConfigMap {
    int width;
    int height;
    int maxSlope;
    int maxLength;
    int waterHeight;
    int heightStart;
    int thicknessMin;
    boolean flyingIsland;
    public ConfigMap(int width, int height, int maxSlope, int maxLength, int waterHeight, int thicknessMin,
            boolean flyingIsland,int heightStart) {
        this.width = width;
        this.height = height;
        this.maxSlope = maxSlope;
        this.maxLength = maxLength;
        this.waterHeight = waterHeight;
        this.thicknessMin = thicknessMin;
        this.flyingIsland = flyingIsland;
        this.heightStart = heightStart;
    }

    
}
