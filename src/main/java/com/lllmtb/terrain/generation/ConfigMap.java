package com.lllmtb.terrain.generation;

import com.lllmtb.physics.GameMode;

public class ConfigMap {
    private final int width;              // the length of map
    private final int height;             // the height of the map (prevents the ground from exceeding this height)
    private final int maxSlope;           // the maximum height difference between two consecutive x-coordinates
    private final int maxLength;          // the maximum length during which the height of each consecutive block will increase by slope
    private final int waterHeight;        // the height of water
    private final int heightStart;        // the desired starting height from the ground
    private final int thicknessMin;       // the minimum map thickness.
    private final boolean flyingIsland;   // true if you want a flying island
    private final int plateauChance;      // the chance of having flat ground (which avoids terrain composed solely of "peaks")
    public static final double RATIOWIDTHHEIGHT = 0.5625; // for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
    private int nbTeam;             //the numbers of Teams.
    private int nbWorms;            //the numbers of worms
    private final GameMode gameMode;      //the gameMode of the game.

    public ConfigMap(int width, int waterHeight, int thicknessMin,
                     boolean flyingIsland, int heightStart, LandScape terrainType, int nbTeam, int nbWorms, GameMode gameMode) {
        this.width = width;
        this.height = (int) (width * RATIOWIDTHHEIGHT); //for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
        this.waterHeight = waterHeight;
        this.thicknessMin = thicknessMin;
        this.flyingIsland = flyingIsland;
        this.heightStart = heightStart;
        this.nbWorms = nbWorms;
        this.nbTeam = nbTeam;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxSlope() {
        return maxSlope;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getWaterHeight() {
        return waterHeight;
    }

    public int getHeightStart() {
        return heightStart;
    }

    public int getThicknessMin() {
        return thicknessMin;
    }

    public boolean isFlyingIsland() {
        return flyingIsland;
    }

    public int getPlateauChance() {
        return plateauChance;
    }

    public int getNbTeam() {
        return nbTeam;
    }

    public int getNbWorms() {
        return nbWorms;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public void setNbTeam(int nbTeam) {
        this.nbTeam = nbTeam;
    }

    public void setNbWorms(int nbWorms) {
        this.nbWorms = nbWorms;
    }
}
