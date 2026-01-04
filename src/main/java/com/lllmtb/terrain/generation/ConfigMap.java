package com.lllmtb.terrain.generation;

import com.lllmtb.physics.GameMode;

public class ConfigMap {


    private int width;              //the length of map
    private int height;             //the height of the map (prevents the ground from exceeding this height)
    private int maxSlope;           //the maximum height difference between two consecutive x-coordinates
    private int maxLength;          //the maximum length during which the height of each consecutive block will increase by slope
    private int waterHeight;        // the height of water
    private int heightStart;        //the desired starting height from the ground
    private int thicknessMin;       // the minimun of the map thickness.
    private boolean flyingIsland;   //true if you want a flying island
    private int plateauChance;      //the chance of having flat ground (which avoids terrain composed solely of "peaks")
    public static final double RATIOWIDTHHEIGHT = 0.5625; //for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
    private GameMode gameMode;      //the gameMode of the game.

    /**
     * the construteur.
     * @param width //the length of map
     * @param waterHeight // the height of water
     * @param thicknessMin  // the minimun of the map thickness.
     * @param flyingIsland //true ifyou want a flying island
     * @param heightStart //the desired starting height from the ground
     * @param terrainType the type of the terrain (FLAT, HILLY...)
     * @param gameMode //the gameMode of the game.
     */
    public ConfigMap(int width, int waterHeight, int thicknessMin,
                     boolean flyingIsland, int heightStart, LandScape terrainType, GameMode gameMode) {
        this.width = width;
        this.height = (int) (width * RATIOWIDTHHEIGHT); //for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
        this.waterHeight = waterHeight;
        this.thicknessMin = thicknessMin;
        this.flyingIsland = flyingIsland;
        this.heightStart = heightStart;
        this.maxSlope = 1;
        this.maxLength = 10;
        this.plateauChance = 20;
        this.gameMode = gameMode;

        switch (terrainType) {

            case FLAT:
                this.maxSlope = 1;
                this.maxLength = 10;
                this.plateauChance = 80;
                break;

            case HILLY:
                this.maxSlope = 2;
                this.maxLength = 3;
                this.plateauChance = 30;
                break;

            case JAGGED:
                this.maxSlope = 3;
                this.maxLength = 5;
                this.plateauChance = 10;
                break;

            case CHAOTIC:
                this.maxSlope = 4;
                this.maxLength = 2;
                this.plateauChance = 0;
                break;

            default:
                System.out.println("attention type inconnu");
                this.maxSlope = 2;
                this.maxLength = 3;
                this.plateauChance = 30;
        }

    }

    /**
     *
     * @return the width of map
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return the height of map
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return the max Slope
     */
    public int getMaxSlope() {
        return maxSlope;
    }

    /**
     *
     * @return the MaxLength during which the height of each consecutive block will increase by slope
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     *
     * @return the height of Water in the map
     */
    public int getWaterHeight() {
        return waterHeight;
    }

    /**
     *
     * @return the starting height of the map generation
     */
    public int getHeightStart() {
        return heightStart;
    }

    /**
     *
     * @return the minimum thickness of land.
     */
    public int getThicknessMin() {
        return thicknessMin;
    }

    /**
     *
     * @return true is the map is a FlyingIsland.
     */
    public boolean isFlyingIsland() {
        return flyingIsland;
    }

    /**
     *
     * @return the the chance to have a pleateau at the generation
     */
    public int getPlateauChance() {
        return plateauChance;
    }

    /**
     *
     * @return the width of map
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }

}
