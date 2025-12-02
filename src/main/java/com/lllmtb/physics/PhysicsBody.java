package com.lllmtb.physics;

import com.lllmtb.model.entity.Entity;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class PhysicsBody {
    private final Entity entity;
    private Position position;
    private Vector vector;
    private final double mass;
    private final double height;
    private final double width;

    /**
     * Constructs a PhysicsBody with the specified parameters.
     * @param entity   The entity associated with this physics body.
     * @param position The initial position of the body.
     * @param vector   The initial velocity vector of the body.
     * @param mass     The mass of the body.
     * @param height   The height of the body.
     * @param width    The width of the body.
     */
    public PhysicsBody(@NonNull Entity entity, @NonNull Position position, @NonNull Vector vector, double mass, double height, double width) {
        this.entity = Objects.requireNonNull(entity);
        this.position = Objects.requireNonNull(position);
        this.vector = Objects.requireNonNull(vector);
        this.mass = mass;
        this.width = width;
        this.height = height;
    }

    /* Getters */
    public Entity getEntity() {
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

    /* Setters */
    public void setPos(@NonNull Position position) {
        this.position = Objects.requireNonNull(position);
    }

    public void setVector(@NonNull Vector vector) {
        this.vector = Objects.requireNonNull(vector);
    }

    @Override
    public String toString() {
        return "Body{ " + position + " | " + vector + " }";
    }
}

