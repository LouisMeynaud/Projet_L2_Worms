package com.lllmtb.terrain;

public class Map implements IMap {

    private int width;
    private int height;
    private Tile.TILES[][] mapMatrix;

    public Map(int height, int width) {
        this.mapMatrix = new Tile.TILES[height][width];
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Tile.TILES[][] getMapMatrix() {
        return this.mapMatrix;
    }

    public Position cooMapToCooMatrix(Position position){

        return new Position(position.getPosx(),this.height -position.getPosy()-1);
    }
    public void setMap(Position position, Tile.TILES tile) {
            Position posmatrix = cooMapToCooMatrix(position);
            this.mapMatrix[(int) posmatrix.getPosy()][(int) posmatrix.getPosx()] = tile; 
    
        }
        

    public boolean isTILES(Tile.TILES tile,Position position) { 
        Position posmatrix = cooMapToCooMatrix(position);
        return this.mapMatrix[(int)posmatrix.getPosy()][(int)posmatrix.getPosx()] == tile; }

    public boolean inMap(double x, double y) {
        return (0 <= x && 0 <= y && x < this.width && y < this.height);
    }

    @Override
    public String toString() {
        String string = "width : " + this.width + ", height : " + this.height + ", matrice : \n";
        for (int i = 0; i < this.height; i++) {
            string += "\n";
            for (int j = 0; j < this.width; j++) {
                string += this.mapMatrix[i][j];
            }
        }
        return string;
    }
}
