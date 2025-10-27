package com.lllmtb.terrain;
import com.lllmtb.physics.Position;



public class Map implements IMap {

    private int width;
    private int height;
    private Tile.TILES[][] mapMatrix;
    private String filesTxt;

    public Map(int height, int width, String filesTxt) {
        this.mapMatrix = new Tile.TILES[height][width];
        this.width = width;
        this.height = height;
        this.filesTxt = filesTxt;
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

    public String getFilesTxt() {
        return filesTxt;
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

    public Position cooMapToCooMatrix(Position position) {

        return new Position(position.getX(), this.height - position.getY() - 1);
    }

    public void setMap(Position position, Tile.TILES tile) {
        Position posmatrix = cooMapToCooMatrix(position);
        this.mapMatrix[(int) posmatrix.getY()][(int) posmatrix.getX()] = tile;

    }

    public boolean isTILES(Tile.TILES tile, Position position) {
        Position posmatrix = cooMapToCooMatrix(position);
        return this.mapMatrix[(int) posmatrix.getY()][(int) posmatrix.getY()] == tile;
    }

    public boolean inMap(double x, double y) {
        return (0 <= x && 0 <= y && x < this.width && y < this.height);
    }

}
