package com.lllmtb.physics;
import java.lang.Math;
public class PhysicsBody {
    private  Position pos;
    private  Position vect   ;

    public  PhysicsBody(Position position, Position vector){
        this.pos = position;
        this.vect = vector;
    }

    public Position getVect() {
        return vect;
    }

    public Position getPos() {
        return pos;
    }
}
