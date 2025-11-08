package com.lllmtb.physics;

public class PhysicsBody {
    private  Position position;
    private Vector vector ;
    private double mass;

    public  PhysicsBody(Position position, Vector vector, double mass) {
        this.position = position;
        this.vector = vector;
        this.mass = mass;
    }

    public double getMass() {return mass;}

    public Vector getVector() {return vector;}

    public Position getPos() {
        return position;
    }

    public void setPos(Position position) {this.position = position;}
    public void setVector(Vector vector) {this.vector = vector;}
}

