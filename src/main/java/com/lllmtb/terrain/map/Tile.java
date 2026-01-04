package com.lllmtb.terrain.map;

public enum Tile {
    DIRT('D', '#', true, 4, 0.90, 0),
    WATER('W', 'o', false, 2, 1.0, -1),
    AIR('A', ' ', false, 1, 1.0, 0),
    CAVERN('C', '~', false, 1, 1.0, 0),
    GRASS('G', '_', true, 2, 0.90, 0),
    UNDERDIRT('U', '#', true, 1, 0.90, 0),
    ICE('I', '#', true, 4, 1, 0),
    SNOW('S', '_', true, 4, 1, 0),
    LAVA('L', 'L', false, 4, 0.90, 0.5),
    TRAP('T', 'T', false, 3, 0.90, 0.5),
    TRAPCAVERN('T', 'T', false, 3, 0.90, 0.5),
    MOONGRASS('M', '_', true, 4, 0.90, 0),
    MOONDIRT('N', '#', true, 4, 0.90, 0),
    MOONUNDERDIRT('X', '#', true, 1, 0.90, 0);

    private final char fileSymbol; // symbole in the save file
    private final char consoleSymbol; // symbole dans l'ui console
    private final boolean solid; // true if the tile is solid
    private final int nbTexture; // number of textures
    private final double friction; // firction of Tile. 1.0 = no friction , 0.0 = full friction
    private final double damage; // damage per tick, -1 if kill instantly

    /**
     * Constructor for Tile enum.
     * @param fileSymbol Symbol in the save file
     * @param consoleSymbol Symbol in the console UI
     * @param solid True if the tile is solid
     * @param nbTexture Number of textures
     * @param friction Friction of Tile. 1.0 = no friction, 0.0 = full friction
     * @param damage the damage that the Tile causes upon contact with
     */
    Tile(char fileSymbol, char consoleSymbol, boolean solid, int nbTexture, double friction, double damage) {
        this.fileSymbol = fileSymbol;
        this.consoleSymbol = consoleSymbol;
        this.solid = solid;
        this.nbTexture = nbTexture;
        this.friction = friction;
        this.damage = damage;
    }

    /**
     * Gets the console symbol of the tile.
     * @return Console symbol character.
     */
    public char getConsoleSymbol() {
        return this.consoleSymbol;
    }

    /**
     * Gets the file symbol of the tile.
     * @return File symbol character.
     */
    public char getFileSymbol() {
        return this.fileSymbol;
    }

    /**
     * Checks if the tile is solid.
     * @return True if the tile is solid, false otherwise.
     */
    public boolean isSolid() {
        return this.solid;
    }

    /**
     * Gets the number of textures for the tile.
     * @return Number of textures.
     */
    public int getNbTexture() {
        return this.nbTexture;
    }

    /**
     * Gets the friction of the tile.
     * @return Friction value. (1.0 = no friction, 0.0 = full friction)
     */
    public double getFriction() {
        return this.friction;
    }

    /**
     *
     * @return the damage that the Tile causes upon contact with
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Gets the Tile enum corresponding to the given file symbol.
     * @param symbol Character symbol from the save file.
     * @return Corresponding Tile enum.
     * @throws IllegalArgumentException if no matching Tile is found.
     */
    public static Tile getByFileSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.fileSymbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Attention , la lettre :" + symbol + " n'a pas de Tile correspondante.");
    }
}