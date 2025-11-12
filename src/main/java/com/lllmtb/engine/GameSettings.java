package com.lllmtb.engine;

public final class GameSettings {
    public final int nbTeams;
    public final String[] teamNames;
    public final int nbWorms;
    public final boolean isWind;
    public final String mapFilePath;
    public final boolean floatingIsland;

    /**
     * @param nbTeams         Number of teams
     * @param teamNames       The name of each team
     * @param nbWorms         Numbers of Worms in a team
     * @param isWind          Is the wind turned on ?
     * @param mapFilePath     Path of the MapFiles (.txt)
     * @param floatingIsland  Is the map generation on Island mode ?
     */
    public GameSettings(int nbTeams, String[] teamNames, int nbWorms, boolean isWind, String mapFilePath, boolean floatingIsland) {
        this.nbTeams = nbTeams;
        this.teamNames = teamNames;
        this.nbWorms = nbWorms;
        this.isWind = isWind;
        this.mapFilePath = mapFilePath;
        this.floatingIsland = floatingIsland;
    }
}
