package com.lllmtb.physics;
public class PhysicsBody {
    private  Position position;
    private Vector vector ;

    public  PhysicsBody(Position position, Vector vector){
        this.position = position;
        this.vector = vector;
    }

    public Vector getVector() {return vector;}

    public Position getPos() {
        return position;
    }

    public void setPos(Position position) {this.position = position;}
    public void setVector(Vector vector) {this.vector = vector;}
}

