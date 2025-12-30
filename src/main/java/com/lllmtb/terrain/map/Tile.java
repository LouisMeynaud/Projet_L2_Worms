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
    MOONGRASS('M', '_', true, 4, 0.90, 0),
    MOONDIRT('N', '#', true, 4, 0.90, 0),
    MOONUNDERDIRT('X', '#', true, 1, 0.90, 0);

    private final char fileSymbol; // symbole in the save file
    private final char consoleSymbol; // symbole dans l'ui console
    private final boolean solid; // true if the tile is solid
    private final int nbTexture; // number of textures
    private final double friction; // firction of Tile. 1.0 = no friction , 0.0 = full friction
    private final double damage; // damage per tick, -1 if kill instantly

    Tile(char fileSymbol, char consoleSymbol, boolean solid, int nbTexture, double friction, double damage) {
        this.fileSymbol = fileSymbol;
        this.consoleSymbol = consoleSymbol;
        this.solid = solid;
        this.nbTexture = nbTexture;
        this.friction = friction;
        this.damage = damage;
    }

    public char getConsoleSymbol() {
        return this.consoleSymbol;
    }

    public char getFileSymbol() {
        return this.fileSymbol;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public int getNbTexture() {
        return this.nbTexture;
    }

    public double getFriction() {
        return this.friction;
    }

    public double getDamage() {
        return this.damage;
    }

    public static Tile getByFileSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.fileSymbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Attention , la lettre :" + symbol + " n'a pas de Tile correspondante.");
    }
}