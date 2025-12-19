package com.lllmtb.physics;

import com.lllmtb.terrain.map.Tile;

public enum GameMode {
    NORMAL(Tile.DIRT,Tile.GRASS,Tile.UNDERDIRT,new Vector(0, -9810)),
    MOON(Tile.MOONDIRT,Tile.MOONGRASS,Tile.MOONDIRT,new Vector(0, -1620)),
    ICEAGE(Tile.ICE,Tile.SNOW,Tile.ICE,new Vector(0, -9810));

    private final Tile grass;
    private final Tile dirt;
    private final Tile underDirt;
    private final Vector gravity;
    

    GameMode(Tile dirt,Tile grass,Tile underDirt,Vector gravity) {
        this.dirt = dirt;
        this.grass = grass;
        this.gravity = gravity;
        this.underDirt = underDirt;
    }

    public Tile getDirt(){
        return this.dirt;
    }
    public Tile getGrass(){
        return this.grass;
    }
    public Tile getUnderDirt(){
        return this.underDirt;
    }
    public Vector getGravity(){
        return this.gravity;
    }
}