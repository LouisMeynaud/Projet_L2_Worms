package com.lllmtb.terrain.generation;

public class ConfigMap {

 
    private int width;              //the length of map
    private int height;             //the height of the map (prevents the ground from exceeding this height)
    private int maxSlope;           //the maximum height difference between two consecutive x-coordinates
    private int maxLength;          //the maximum length during which the height of each consecutive block will increase by slope
    private int waterHeight;        // the height of water
    private int heightStart;        //the desired starting height from the ground
    private int thicknessMin;       // the minimun of the map thickness.
    private boolean flyingIsland;   //true ifyou want a flying island
    private int plateauChance;      //the chance of having flat ground (which avoids terrain composed solely of "peaks")
    public static final double RATIOWIDTHHEIGHT = 0.5625; //for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
    private int nbTeam;
    private int nbWarms;
    
    public ConfigMap(int width, int waterHeight, int thicknessMin,
            boolean flyingIsland,int heightStart,LandScape terrainType,int nbTeam,int nbWarms) {
        this.width = width;
        this.height = (int) (width * RATIOWIDTHHEIGHT); //for a rectangular world (0.5625 is the ratio between the width and heigth of a commun screen)
        this.waterHeight = waterHeight;
        this.thicknessMin = thicknessMin;
        this.flyingIsland = flyingIsland;
        this.heightStart = heightStart;
        this.maxSlope = 1;
        this.maxLength = 10;
        this.plateauChance = 20;
        this.nbWarms= nbWarms;
        this.nbTeam= nbTeam;
        
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
            
            case CHAOTIC: this.maxSlope = 4;
                this.maxLength = 2;
                this.plateauChance = 0;
                break;
            
            default:
                System.out.println("attention type inconnu");
                this.maxSlope = 2;
                this.maxLength = 3;
                this.plateauChance =30;
                ;
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

    public int getNbWarms() {
        return nbWarms;
    }

    public void setNbTeam(int nbTeam) {
        this.nbTeam = nbTeam;
    }

    public void setNbWarms(int nbWarms) {
        this.nbWarms = nbWarms;
    }

   

    

    


    

    
}
