package com.lllmtb.terrain.map;
import com.lllmtb.physics.Position;



public interface IMap {
    
    public int getWidth();
    public int getHeight();
    public Tile[][] getMapMatrix();

    
    public Tile getTile(Position position);

  
    public void setTile(Position position, Tile tile);

    public Position cooMapToCooMatrix(Position position);
    
    public boolean isTileAt(Position position, Tile tile);

    
    public boolean inMap(double x, double y);

    public String toString();
}
