package com.lllmtb.terrain.map;
import com.lllmtb.physics.Position;

public class Map {
/**
 Represent the game cards as a matrix of TILES,
* representing the different elements present at a given x y position.
* A cell in the matrix is ​​represented by an integer x y coordinate (0,0)
* in the bottom left corner.

 */
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

    protected void setFilesTxt(String files){
        this.filesTxt = files;
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
                    default : throw new IllegalStateException("Tile inconnue : " + mapMatrix[i][j]);
                }
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
        Position posMatrix = cooMapToCooMatrix(position);
        return this.mapMatrix[(int) posMatrix.getY()][(int) posMatrix.getX()];
    }

    /**
     * Setter modifying a case of the matrix.
     * 
     * @param position, the position or the tile will be changed
     * @param TILES , the Tile by which the case will be changed
     */
    public void setTile(Position position, Tile tile) {
        Position posMatrix = cooMapToCooMatrix(position);
        this.mapMatrix[(int) posMatrix.getY()][(int) posMatrix.getX()] = tile;

    }
    /**
     *
     * @param TILES    The Tile whose presence we are checking.
     * @param position the checking position
     */
    public boolean isTileAt(Position position, Tile tile) {
        return getTile(position) == tile;
    }

    /**
     * verify that the x,y coordinate is indeed within the map (the matrix)
     */
    public boolean inMap(Position position) {
        return (0 <= position.getX() && 0 <= position.getY() && position.getX() < this.width && position.getY() < this.height);
    }

}
