package com.lllmtb.Engine;

public class GameSettings {
    public final int nbTeams; //Number of teams
    public final String[] TeamNames;// The name of the different team
    public final int numberWorms;// Numbers of Worms in a team
    public final boolean isWind; // Is the wind turn on ?
    public final String mapFilePath;// Path of the MapFiles (.txt)
    public final boolean GenerateIsland; // Is the map generation on Island mode ?

    public GameSettings(int nbTeams,String[] TeamNames,int numberWorms,boolean isWind,String mapFilePath,boolean GenerateIsland ) {
        this.nbTeams = nbTeams;
        this.TeamNames = TeamNames;
        this.numberWorms = numberWorms;
        this.isWind = isWind;
        this.mapFilePath = mapFilePath;
        this.GenerateIsland = GenerateIsland;
    }
}
