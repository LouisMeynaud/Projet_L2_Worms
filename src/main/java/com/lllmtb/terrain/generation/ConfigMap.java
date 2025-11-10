package com.lllmtb.terrain.generation;

public class ConfigMap {

 
    int width;              //the length of map
    int height;             //the height of the map (prevents the ground from exceeding this height)
    int maxSlope;           //the maximum height difference between two consecutive x-coordinates
    int maxLength;          //the maximum length during which the height of each consecutive block will increase by slope
    int waterHeight;        // the height of water
    int heightStart;        //the desired starting height from the ground
    int thicknessMin;       // the minimun of the map thickness.
    boolean flyingIsland;   //true ifyou want a flying island
    int plateauChance;      //the chance of having flat ground (which avoids terrain composed solely of "peaks")
    public ConfigMap(int width, int height, int maxSlope, int maxLength, int waterHeight, int thicknessMin,
            boolean flyingIsland,int heightStart,int plateauChance) {
        this.width = width;
        this.height = height;
        this.maxSlope = maxSlope;
        this.maxLength = maxLength;
        this.waterHeight = waterHeight;
        this.thicknessMin = thicknessMin;
        this.flyingIsland = flyingIsland;
        this.heightStart = heightStart;
        this.plateauChance = plateauChance;
    }

    
}
