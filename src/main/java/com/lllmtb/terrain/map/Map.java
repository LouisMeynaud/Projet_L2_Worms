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

    /**
     * Constructor for Map class.
     * @param height Height of the map.
     * @param width Width of the map.
     * @param filesTxt Filename associated with the map.
     */
    public Map(int height, int width, String filesTxt) {
        this.mapMatrix = new Tile[height][width];
        this.width = width;
        this.height = height;
        this.filesTxt = filesTxt;
        this.spawnPoint = null;
    }

    /**
     * Gets the width of the map.
     * @return Width of the map.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the height of the map.
     * @return Height of the map.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the matrix of tiles representing the map.
     * @return array of Tile objects.
     */
    public Tile[][] getMapMatrix() {
        return this.mapMatrix;
    }

    /**
     * Gets the filename associated with the map.
     * @return Filename as a String.
     */
    public String getFilesTxt() {
        return filesTxt;
    }

    /**
     * Sets the filename associated with the map.
     * @param files Filename as a String.
     */
    protected void setFilesTxt(String files) {
        this.filesTxt = files;
    }

    /**
     * Sets the spawn points for the map.
     * @param spawnPoint 2D array of Position representing different spawn points.
     */
    public void setSpawnPoint(Position[][] spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    /**
     * Provides a string representation of the map, including its dimensions and tile layout.
     * @return String representation of the map.
     */
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


    /**
     * Setter modifying the height of the water.
     * @param heightWater the new height of the water
     */
    public void setHeightWater(int heightWater) {
        this.heightWater = heightWater;
    }

    /**
     *
     * @param position the checking position
     * @param tile     the Tile whose presence we are checking.
     * @return true if the tile at the position is of the specified Tile, false otherwise.
     */
    public boolean isTileAt(Position position, Tile tile) {

        return getTile(position) == tile;
    }

    /**
     * verify if the x,y coordinate is in the map (the matrix).
     * @param position , the checking position
     * @return true if the position is in the map, false otherwise.
     */
    public boolean inMap(Position position) {
        position = this.cooMapToCooMatrix(position);
        return (0 <= position.getX() && 0 <= position.getY() && position.getX() < this.width && position.getY() < this.height);
    }

    /**
     *
     * @param position the checking position
     * @return true if the tile at the position is solid, false otherwise.
     */
    public boolean isSolid(Position position) {
        return this.getTile(position).isSolid();
    }
    /**
     *
     * @param position the position we want to test
     * @return if the Tile at the position take damage
     */
    public boolean isDamaging(Position position) {
        Tile tile = this.getTile(position);
        return tile.getDamage() > 0;
    }

    /**
     * Gets the spawn points for the map.
     * @return 2D array of Position representing different spawn points.
     */
    public Position[][] getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Gets the current height of the water in the map.
     * @return Height of the water.
     */
    public int getHeightWater() {
        return heightWater;
    }

    /**
     * Initializes the height of the water based on the map layout.
     */
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

    /**
     * Raises the water level by one unit if possible.
     * @param height the height to raise the water level.
     * @return true if the water level was raised, false if it is already at maximum height.
     */
    public boolean risingWater(int height) {
        if (this.heightWater < this.height) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < this.width; i++) {
                    this.setTile(new Position(i, this.heightWater), Tile.WATER);
                }
                this.heightWater++;
            }
            return true;
        }
        return false;
    }
}
