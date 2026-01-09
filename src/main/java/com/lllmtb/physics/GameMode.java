package com.lllmtb.physics;

import com.lllmtb.terrain.map.Tile;

public enum GameMode {
    NORMAL(Tile.DIRT, Tile.GRASS, Tile.UNDERDIRT, new Vector(0, -9810)),
    MOON(Tile.MOONDIRT, Tile.MOONGRASS, Tile.MOONUNDERDIRT, new Vector(0, -1620)),
    ICEAGE(Tile.ICE, Tile.SNOW, Tile.ICEUNDERDIRT, new Vector(0, -9810));

    private final Tile grass;
    private final Tile dirt;
    private final Tile underDirt;
    private final Vector gravity;


    GameMode(Tile dirt, Tile grass, Tile underDirt, Vector gravity) {
        this.dirt = dirt;
        this.grass = grass;
        this.gravity = gravity;
        this.underDirt = underDirt;
    }

    /**
     *
     * @return the "Dirt" for this gameMode.
     */
    public Tile getDirt() {
        return this.dirt;
    }
    /**
     *
     * @return the "Grass" for this gameMode.
     */
    public Tile getGrass() {
        return this.grass;
    }
     /**
     *
     * @return the "underDirt" for this gameMode.
     */
    public Tile getUnderDirt() {
        return this.underDirt;
    }
     /**
     *
     * @return the Gravity constant for this gameMode.
     */
    public Vector getGravity() {
        return this.gravity;
    }
}