package com.lllmtb.terrain.map;

public enum Tile {
    DIRT('D', '#', true, 3),
    WATER('W', 'o', false, 2),
    AIR('A', ' ', false, 1),
    CAVERN('C', '~', false, 1),
    GRASS('G', '_', true, 2);

    private final char fileSymbol;
    private final char consoleSymbol;
    private final boolean solid;
    private final int nbTexture;

    Tile(char fileSymbol, char consoleSymbol, boolean solid, int nbTexture) {
        this.fileSymbol = fileSymbol;
        this.consoleSymbol = consoleSymbol;
        this.solid = solid;
        this.nbTexture = nbTexture;
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


    public static Tile getByFileSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.fileSymbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Attention , la lettre :" + symbol + " n'a pas de Tile correspondante.");
    }
}