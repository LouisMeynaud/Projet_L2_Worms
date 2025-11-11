package com.lllmtb.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsBody {
    private final Entity entity;
    private Position position;
    private Vector vector;
    private final double mass;
    private final double height;
    private final double width;

    public PhysicsBody(Entity entity, Position position, Vector vector, double mass, double height, double width) {
        this.entity = entity;
        this.position = position;
        this.vector = vector;
        this.mass = mass;
        this.width = width;
        this.height= height;
    }

    public Entity getEntity(){
        return entity;
    }

    public Position getPos() {
        return position;
    }

    public Vector getVector() {
        return vector;
    }

    public double getMass() {
        return mass;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setPos(Position position) {
        this.position = position;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
}

