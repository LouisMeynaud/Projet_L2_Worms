package com.lllmtb.terrain;
import com.lllmtb.physics.Position;

public class Map implements IMap {

    private int width;
    private int height;
    private Tile[][] mapMatrix;
    private String filesTxt;

    public Map(int height, int width, String filesTxt) {
        this.mapMatrix = new Tile[height][width];
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

    public Tile[][] getMapMatrix() {
        return this.mapMatrix;
    }

    public String getFilesTxt() {
        return filesTxt;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("width : " + this.width + ", height : " + this.height + ", matrice : \n");
        for (int i = 0; i < this.height; i++) {
            string.append("\n");
            for (int j = 0; j < this.width; j++) {
                switch (this.mapMatrix[i][j]) {
                    case AIR: string.append(" ");break;
                    case DIRT : string.append("#");break;
                    case WATER : string.append("o");break;
                }
            }
        }
        return string.toString();
    }

    public Position cooMapToCooMatrix(Position position) {
        return new Position(position.getX(), this.height - position.getY() - 1);
    }

    public Tile getTile(Position position) {
        Position posMatrix = cooMapToCooMatrix(position);
        return this.mapMatrix[(int) posMatrix.getY()][(int) posMatrix.getX()];
    }

    public void setTile(Position position, Tile tile) {
        Position posMatrix = cooMapToCooMatrix(position);
        this.mapMatrix[(int) posMatrix.getY()][(int) posMatrix.getX()] = tile;

    }

    public boolean isTileAt(Position position, Tile tile) {
        return getTile(position) == tile;
    }

    public boolean inMap(double x, double y) {
        return (0 <= x && 0 <= y && x < this.width && y < this.height);
    }

}
