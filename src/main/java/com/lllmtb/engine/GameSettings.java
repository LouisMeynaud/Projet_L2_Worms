package com.lllmtb.engine;

import com.lllmtb.terrain.generation.LandScape;

public final class GameSettings {
    public final int nbTeams;
    public final String[] teamNames;
    public final int nbWorms;
    public final boolean hasWind;
    public final String mapFileName;
    public final boolean floatingIsland;
    public final int widthMap;
    public final LandScape terrainType;
    public final long turnDuration;
    public final int maxjump;

    /**
     * @param nbTeams         Number of teams
     * @param teamNames       The name of each team
     * @param nbWorms         Numbers of Worms in a team
     * @param turnDuration    Duration of each turn in seconds
     * @param hasWind         Is the wind turned on ?
     * @param mapFileName     Name of the map file to load (null to generate a new map)
     * @param floatingIsland  Is the map generation on Island mode ?
     * @param widthMap        Width of the map to generate (ignored if mapFileName is not null)
     * @param terrainType     Type of terrain to generate
     */
    public GameSettings(int nbTeams, String[] teamNames, int nbWorms, long turnDuration, boolean hasWind, String mapFileName, boolean floatingIsland, int widthMap, LandScape terrainType,int maxjump) {
        this.nbTeams = nbTeams;
        this.teamNames = teamNames;
        this.nbWorms = nbWorms;
        this.hasWind = hasWind;
        this.mapFileName = mapFileName;
        this.floatingIsland = floatingIsland;
        this.widthMap = widthMap;
        this.terrainType = terrainType;
        this.turnDuration = turnDuration;
        this.maxjump = maxjump;
    }
}
