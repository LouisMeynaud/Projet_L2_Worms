package com.lllmtb.physics;

public class Vector {

    private final double x;
    private final double y;


    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(){
        this(0, 0);
    }

    public double getX() {  return this.x;  }
    public double getY() { return this.y; }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector sub(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    public Vector scale(double s) {
        return new Vector(this.x * s, this.y * s);
    }

    public boolean equals(Vector other){
        if (other == null) return false;
        return this.x == other.x && this.y == other.y;
    }

    public Position newPos(Position pos, double time) {
        return new Position(pos.getX() + this.x*time, pos.getY()+ this.y*time );
    }

    @Override
    public String toString() {
        return "Vect("+ this.x + "," + this.y + ")";
    }



}
