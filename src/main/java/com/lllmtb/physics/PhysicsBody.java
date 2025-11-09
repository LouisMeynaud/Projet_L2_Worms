package com.lllmtb.physics;

public class PhysicsBody {
    private  Position position;
    private Vector vector ;
    private double mass;
    private int height;
    private int width;

    public  PhysicsBody(Position position, Vector vector, double mass,int height, int width) {
        this.position = position;
        this.vector = vector;
        this.mass = mass;
        this.width = width;
        this.height= height;
    }

    public int getHeight() {return height;}

    public int getWidth() {return width;}

    public double getMass() {return mass;}

    public Vector getVector() {return vector;}

    public Position getPos() {
        return position;
    }

    public void setPos(Position position) {this.position = position;}
    public void setVector(Vector vector) {this.vector = vector;}
}

