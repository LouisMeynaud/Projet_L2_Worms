package com.lllmtb.physics;

import com.lllmtb.model.entity.Entity;

public class PhysicsBody {
    private final Entity entity;
    private Position position;
    private Vector vector;
    private final double mass;
    private final double height;
    private final double width;
    private final double radius;

    public PhysicsBody(Entity entity, Position position, Vector vector, double mass, double height, double width) {
        this.entity = entity;
        this.position = position;
        this.vector = vector;
        this.mass = mass;
        this.width = width;
        this.height = height;
        this.radius = Math.max(this.height, this.width) / 2.0;
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

    /**
     * @return the {@code double} which represents the radius of the circle that is the
     * hitbox of the entity
     */
    public double getRadius() {return this.radius;}

    public void setPos(Position position) {
        this.position = position;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "Body{ " + position + " | " + vector + " }";
    }
}

