package com.lllmtb.terrain.map;

public enum Tile {
    DIRT('D', '#', true),
    WATER('W', 'o', false),
    AIR('A', ' ', false),
    CAVERN('C', '~', false),
    GRASS('G', '_', true);

    private final char fileSymbol;
    private final char consoleSymbol;
    private final boolean solid;

    Tile(char fileSymbol, char consoleSymbol, boolean solid) {
        this.fileSymbol = fileSymbol;
        this.consoleSymbol = consoleSymbol;
        this.solid = solid;
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


    public static Tile getByFileSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.fileSymbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Attention , la lettre :" + symbol + " n'a pas de Tile correspondante.");
    }
}