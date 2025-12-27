package com.lllmtb.terrain.map;

import com.lllmtb.physics.Position;

public class Map {
    /**
     * Represent the game cards as a matrix of TILES,
     * representing the different elements present at a given x y position.
     * A cell in the matrix is ​​represented by an integer x y coordinate (0,0)
     * in the bottom left corner.
     */
    private int width;
    private int height;
    private Tile[][] mapMatrix;
    private String filesTxt;
    private Position[][] spawnPoint;
    private int heightWater = 0; //devra etre changer

    public Map(int height, int width, String filesTxt) {
        this.mapMatrix = new Tile[height][width];
        this.width = width;
        this.height = height;
        this.filesTxt = filesTxt;
        this.spawnPoint = null;
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


    protected void setFilesTxt(String files) {
        this.filesTxt = files;
    }


    public void setSpawnPoint(Position[][] spawnPoint) {
        this.spawnPoint = spawnPoint;
    }


    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("width : " + this.width + ", height : " + this.height + ", matrice : \n");
        for (int i = 0; i < this.height; i++) {
            string.append("\n");
            for (int j = 0; j < this.width; j++) {
               string.append(this.mapMatrix[i][j].getConsoleSymbol());
            }
        }
        return string.toString();
    }

    /**
     * @param position
     * @return a new position corresponding to the case in the matrix
     * (because in the game 0,0 is in the bottom left corner, whereas in the matrix (0,0) is in the top left corner)
     */
    public Position cooMapToCooMatrix(Position position) {
        return new Position(position.getX(), this.height - position.getY() - 1);
    }

    /**
     *
     * @param position
     * @return the tile at the position.
     */
    public Tile getTile(Position position) {
        Position posMatrix = this.cooMapToCooMatrix(position);
        int x = (int) posMatrix.getX();
        int y = (int) posMatrix.getY();

        if (y < 0 || y >= this.mapMatrix.length || x < 0 || x >= this.mapMatrix[0].length) {
            return Tile.AIR;
        }

        return this.mapMatrix[y][x];
    }

    /**
     * Setter modifying a case of the matrix.
     * @param position the position or the tile will be changed
     * @param tile     the Tile by which the case will be changed
     */
    public void setTile(Position position, Tile tile) {
        Position posMatrix = this.cooMapToCooMatrix(position);
        this.mapMatrix[(int) posMatrix.getY()][(int) posMatrix.getX()] = tile;

    }



    public void setHeightWater(int heightWater) {
        this.heightWater = heightWater;
    }

    /**
     *
     * @param position the checking position
     * @param tile     the Tile whose presence we are checking.
     */
    public boolean isTileAt(Position position, Tile tile) {

        return getTile(position) == tile;
    }

    /**
     * verify if the x,y coordinate is in the map (the matrix)
     * @param position , the checking position
     */
    public boolean inMap(Position position) {
        position = this.cooMapToCooMatrix(position);
        return (0 <= position.getX() && 0 <= position.getY() && position.getX() < this.width && position.getY() < this.height);
    }

    public boolean isSolid(Position position) {
        return this.getTile(position).isSolid();
    }

    public Position[][] getSpawnPoint() {
        return spawnPoint;
    }

    public int getHeightWater() {
        return heightWater;
    }
    public void initializeHeightWater() {
        boolean find = false;
        int i = 0;
        while (!find) {
            if (!this.isTileAt(new Position(0, i), Tile.WATER)) {
                find = true;
                break;
            }
            i++;
        } this.heightWater = i;
    }

    public boolean risingWater() {
        if (this.heightWater < this.height) {
            for (int i = 0; i < this.width; i++) {
            this.setTile(new Position(i, this.heightWater), Tile.WATER);
            }
            this.heightWater++;
            return true;
        }
        return false;
    }
}
