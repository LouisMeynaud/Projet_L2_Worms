package com.lllmtb.physics;

import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * A class representing a 2D vector with basic vector operations.
 */
public class Vector {
    private final double x;
    private final double y;

    /**
     * Constructs a Vector with the specified x and y components.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a zero vector (0, 0).
     */
    public Vector(){
        this(0, 0);
    }

    /** Getters */
    public double getX() { return this.x; }
    public double getY() { return this.y; }

    /**
     * Adds another vector to this vector.
     *
     * @param other the vector to add
     * @return a new Vector representing the sum of the two vectors
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public Vector add(@NonNull Vector other) {
        Objects.requireNonNull(other);
        return new Vector(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other the vector to subtract
     * @return a new Vector representing the difference of the two vectors
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public Vector sub(@NonNull Vector other) {
        Objects.requireNonNull(other);
        return new Vector(this.x - other.x, this.y - other.y);
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param s the scalar value to scale by
     * @return a new Vector representing the scaled vector
     */
    public Vector scale(double s) {
        return new Vector(this.x * s, this.y * s);
    }

    /**
     * Checks if this vector is equal to another vector.
     *
     * @param other the vector to compare with
     * @return true if the vectors are equal, false otherwise
     */
    public boolean equals(Vector other){
        if (other == null) return false;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Calculates a new position based on the current position and this vector over a given time.
     *
     * @param pos  the current position
     * @param time the time duration
     * @return a new Position after applying the vector over the specified time
     * @throws NullPointerException if {@code pos} is {@code null}
     */
    public Position newPos(@NonNull Position pos, double time) {
        Objects.requireNonNull(pos);
        return new Position(pos.getX() + this.x*time, pos.getY() + this.y*time);
    }

    @Override
    public String toString() {
        return "Vect("+ this.x + "," + this.y + ")";
    }
}
