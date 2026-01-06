package com.lllmtb.engine;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;

public final class GameSettings {
    public final int nbTeams;
    public final String[] teamNames;
    public final boolean[] isBot;
    public final int nbWorms;
    public final boolean hasWind;
    public final String mapFileName;
    public final boolean floatingIsland;
    public final int widthMap;
    public final LandScape terrainType;
    public final long turnDuration;
    public final String gameName;
    public final int maxJump;
    public final double maxHp;
    public GameMode gameMode;
    public final boolean friendlyfire;
    public final boolean coinCheat;


    /**
     * @param nbTeams        Number of teams
     * @param teamNames      The name of each team
     * @param isBot         Array indicating if each team is a bot
     * @param nbWorms        Numbers of Worms in a team
     * @param turnDuration   Duration of each turn in seconds
     * @param hasWind        Is the wind turned on ?
     * @param mapFileName    Name of the map file to load (null to generate a new map)
     * @param floatingIsland Is the map generation on Island mode ?
     * @param widthMap       Width of the map to generate (ignored if mapFileName is not null)
     * @param terrainType    Type of terrain to generate
     * @param maxJump        Maximum jump height for worms
     * @param gameName       Name of the game session
     * @param friendlyfire   Boolean if friendly fire is activated
     * @param maxHp         Maximum health points for worms
     * @param gameMode      Game mode selected
     * @param coinCheat     If coin cheat is activated
     */


    public GameSettings(String gameName, int nbTeams, String[] teamNames, boolean[] isBot, int nbWorms, double maxHp, long turnDuration, boolean hasWind, String mapFileName,
        boolean floatingIsland, int widthMap, LandScape terrainType, int maxJump, GameMode gameMode, boolean friendlyfire, boolean coinCheat) {
        this.nbTeams = nbTeams;
        this.teamNames = teamNames;
        this.isBot = isBot;
        this.nbWorms = nbWorms;
        this.hasWind = hasWind;
        this.mapFileName = mapFileName;
        this.floatingIsland = floatingIsland;
        this.widthMap = widthMap;
        this.terrainType = terrainType;
        this.turnDuration = turnDuration;
        this.gameName = gameName;
        this.maxJump = maxJump;
        this.maxHp = maxHp;
        this.gameMode = gameMode;
        this.friendlyfire = friendlyfire;
        this.coinCheat = coinCheat;
    }
}
