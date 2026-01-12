package com.lllmtb.engine;

import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;

import java.util.Objects;

public final class GameSettings {
    public final int nbTeams;
    public final String[] teamNames;
    public final boolean[] isBot;
    public final int nbWorms;
    public final boolean hasWind;
    public final String mapFileName;
    public final LandScape terrainType;
    public final long turnDuration;
    public final String gameName;
    public final int maxJump;
    public final double maxHp;
    public GameMode gameMode;
    public final boolean friendlyFire;
    public final boolean coinCheat;
    public final ConfigMap configMap;

    /**
     * @param nbTeams        Number of teams
     * @param teamNames      The name of each team
     * @param isBot          Array indicating if each team is a bot
     * @param nbWorms        Numbers of Worms in a team
     * @param turnDuration   Duration of each turn in seconds
     * @param hasWind        Is the wind turned on ?
     * @param mapFileName    Name of the map file to load (null to generate a new map)
     * @param maxJump        Maximum jump height for worms
     * @param gameName       Name of the game session
     * @param friendlyFire   Boolean if friendly fire is activated
     * @param maxHp         Maximum health points for worms
     * @param gameMode      Game mode selected
     * @param coinCheat     If coin cheat is activated
     * @param configMap      Configuration of the map (null if loading a map)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public GameSettings(String gameName, int nbTeams, String[] teamNames, boolean[] isBot, int nbWorms, double maxHp, long turnDuration, boolean hasWind, String mapFileName,
                         ConfigMap configMap, int maxJump, GameMode gameMode, boolean friendlyFire, boolean coinCheat) {


        // Validate inputs
        if (gameName == null || gameName.isBlank()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }
        if (nbTeams < 1) {
            throw new IllegalArgumentException("Number of teams must be at least 1, got: " + nbTeams);
        }
        if (nbWorms < 1) {
            throw new IllegalArgumentException("Number of worms per team must be at least 1, got: " + nbWorms);
        }
        if (teamNames == null || teamNames.length < nbTeams) {
            throw new IllegalArgumentException("Team names array must have at least " + nbTeams + " elements");
        }
        if (isBot == null || isBot.length < nbTeams) {
            throw new IllegalArgumentException("Bot state array must have at least " + nbTeams + " elements");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException("Max HP must be positive, got: " + maxHp);
        }
        if (turnDuration <= 0) {
            throw new IllegalArgumentException("Turn duration must be positive, got: " + turnDuration);
        }
        if (mapFileName == null) { // generating a new map
            Objects.requireNonNull(configMap, "ConfigMap cannot be null when generating a map");
        }

        Objects.requireNonNull(gameMode, "Game mode cannot be null");

        this.nbTeams = nbTeams;
        this.teamNames = teamNames;
        this.isBot = isBot;
        this.nbWorms = nbWorms;
        this.hasWind = hasWind;
        this.mapFileName = mapFileName;
        this.configMap = configMap;
        this.terrainType = (configMap == null) ? LandScape.HILLY : configMap.terrainType;
        this.turnDuration = turnDuration;
        this.gameName = gameName;
        this.maxJump = maxJump;
        this.maxHp = maxHp;
        this.gameMode = gameMode;
        this.friendlyFire = friendlyFire;
        this.coinCheat = coinCheat;
    }
}
