package com.lllmtb.terrain.map;

public enum Tile {
    DIRT('D', '#', true, 4, 0.90),
    WATER('W', 'o', false, 2, 1.0),
    AIR('A', ' ', false, 1, 1.0),
    CAVERN('C', '~', false, 1, 1.0),
    GRASS('G', '_', true, 2, 0.90),
    UNDERDIRT('U', '#', true, 1, 0.90),
    ICE('I', '#', true, 4, 1),
    SNOW('S', '_', true, 4, 1),
    LAVA('L', 'L', false, 4, 0.90),
    MOONGRASS('M', '_', true, 4, 0.90),
    MOONDIRT('N', '#', true, 4, 0.90),
    MOONUNDERDIRT('X', '#', true, 1, 0.90);

    private final char fileSymbol; // symbole in the save file
    private final char consoleSymbol; // symbole dans l'ui console
    private final boolean solid; // true if the tile is solid
    private final int nbTexture; // number of textures
    private final double friction; // firction of Tile. 1.0 = no friction , 0.0 = full friction

    Tile(char fileSymbol, char consoleSymbol, boolean solid, int nbTexture, double friction) {
        this.fileSymbol = fileSymbol;
        this.consoleSymbol = consoleSymbol;
        this.solid = solid;
        this.nbTexture = nbTexture;
        this.friction = friction;
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


    public static Tile getByFileSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.fileSymbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Attention , la lettre :" + symbol + " n'a pas de Tile correspondante.");
    }
}