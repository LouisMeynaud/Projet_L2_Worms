package com.lllmtb.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsBody {
    private final Entity entity;
    private  Position position;
    private Vector vector ;
    private double mass;
    private int height;
    private int width;

    public  PhysicsBody(Entity entity,Position position, Vector vector, double mass,int height, int width) {
        this.entity = entity;
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
    
    public Entity getEntity(){return entity;}

    public void setPos(Position position) {this.position = position;}
    public void setVector(Vector vector) {this.vector = vector;}
}

